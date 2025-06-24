package com.example.expense.expense_tracker.service;

import com.example.expense.expense_tracker.expense_entity.DailySummaryDTO;
import com.example.expense.expense_tracker.expense_entity.MonthlyDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MascotTipsService {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public MascotTipsService(ExpenseService expenseService, IncomeService incomeService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }
    private static final Map<String,List<String>> TEMPLATES = new HashMap<>();
    static {
        TEMPLATES.put("BIG_CATEGORY", Arrays.asList(
                "Whoa—${amt} on ${cat}? That’s ${pct}% of this month’s spend. Maybe trim it back a bit.",
                "Heads up: ${cat} makes up ${pct}% ($${amt}) of your expenses. Could you cut back?",
                "You’ve spent $${amt} on ${cat}, or ${pct}% of total. Consider reining it in."
        ));
        TEMPLATES.put("TOP3_CATEGORIES", Arrays.asList(
                "Your #2 spender is ${cat2} at ${pct2}% of this month’s total.",
                "Second largest spend: ${cat2} ($${amt2}, ${pct2}%). Is that intentional?",
                "Aside from ${cat1}, you spent $${amt2} on ${cat2}, about ${pct2}% of expenses."
        ));
        TEMPLATES.put("MONTHLY_TREND", Arrays.asList(
                "Great job! You ${trendVerb} spending by ${chgPct}% vs last month.",
                "You ${trendVerb} your expenses from $${last} to $${this}—a ${chgPct}% change.",
                "Expenses are ${chgPct}% ${trendAdj} than last month ($${last} → $${this})."
        ));
        TEMPLATES.put("YTD_THRESHOLD", Arrays.asList(
                "You’ve used $${ytd} so far this year—${pctYtd}% of a $100k budget.",
                "Year-to-date spending is $${ytd}. That’s ${pctYtd}% of your annual target.",
                "At $${ytd} YTD, you’re ${pctYtd}% through your yearly budget."
        ));
    }


    public List<String> generateTipsForUser(){

        DailySummaryDTO monthSummary = expenseService.getCurrentMonth(0, Integer.MAX_VALUE);
        double totalThisMonth = monthSummary.getTotalExpense();
        double ytdExpense     = monthSummary.getThisYear();

        Map<String, Double> catSummary =
                expenseService.getCategorySummaryForCurrentMonth(0, Integer.MAX_VALUE);

        List<MonthlyDTO> monthlyTotals = expenseService.getTotalExpenseByMonth();

        LocalDate now = LocalDate.now();
        int year  = now.getYear();
        int month = now.getMonthValue();
        double thisMonthTotal = 0;
        double lastMonthTotal = 0;
        for (MonthlyDTO m : monthlyTotals) {
            if (m.getYear() == year && m.getMonth() == month) {
                thisMonthTotal = m.getTotalAmount();
            }
            int lmYear  = (month == 1) ? year - 1 : year;
            int lmMonth = (month == 1) ? 12 : month - 1;
            if (m.getYear() == lmYear && m.getMonth() == lmMonth) {
                lastMonthTotal = m.getTotalAmount();
            }
        }


        List<String> fired = new ArrayList<>();


        if (totalThisMonth > 0 && !catSummary.isEmpty()) {
            String topCat = null;
            double topAmt = 0;
            for (Map.Entry<String, Double> entry : catSummary.entrySet()) {
                if (entry.getValue() > topAmt) {
                    topAmt = entry.getValue();
                    topCat = entry.getKey();
                }
            }
            if (topAmt / totalThisMonth > 0.3) {
                fired.add("BIG_CATEGORY");
            }
        }


        List<Map.Entry<String, Double>> sortedCats = new ArrayList<>(catSummary.entrySet());
        Collections.sort(sortedCats, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return Double.compare(o2.getValue(), o1.getValue());
            }
        });
        if (sortedCats.size() >= 2) {
            double secondAmt = sortedCats.get(1).getValue();
            if (secondAmt / totalThisMonth > 0.15) {
                fired.add("TOP3_CATEGORIES");
            }
        }


        if (lastMonthTotal > 0) {
            double changePct = (thisMonthTotal - lastMonthTotal) / lastMonthTotal * 100;
            if (Math.abs(changePct) > 5) {
                fired.add("MONTHLY_TREND");
            }
        }


        double pctYtd = ytdExpense / 100_000 * 100;
        if (pctYtd > 50) {
            fired.add("YTD_THRESHOLD");
        }


        if (fired.isEmpty()) {
            return Collections.singletonList("All looks good this month—keep up the great work!");
        }


        Collections.shuffle(fired);
        List<String> tips = new ArrayList<>();
        int count = Math.min(2, fired.size());
        for (int i = 0; i < count; i++) {
            String rule = fired.get(i);
            String template = pickRandomTemplate(rule);
            tips.add(populateTemplate(rule, template, totalThisMonth, ytdExpense,
                    thisMonthTotal, lastMonthTotal, sortedCats, pctYtd));
        }
        return tips;
    }

    private String pickRandomTemplate(String rule) {
        List<String> pool = TEMPLATES.get(rule);
        int idx = new Random().nextInt(pool.size());
        return pool.get(idx);
    }

    private String populateTemplate(
            String rule,
            String tpl,
            double totalThisMonth,
            double ytdExpense,
            double thisMonthTotal,
            double lastMonthTotal,
            List<Map.Entry<String, Double>> sortedCats,
            double pctYtd
    ) {
        if ("BIG_CATEGORY".equals(rule)) {
            Map.Entry<String, Double> top = sortedCats.get(0);
            String cat = top.getKey();
            double amt = top.getValue();
            int pct = (int) Math.round(amt / totalThisMonth * 100);
            return tpl.replace("${cat}", cat)
                    .replace("${amt}", String.format("%.2f", amt))
                    .replace("${pct}", String.valueOf(pct));
        }
        if ("TOP3_CATEGORIES".equals(rule)) {
            Map.Entry<String, Double> first  = sortedCats.get(0);
            Map.Entry<String, Double> second = sortedCats.get(1);
            String cat1 = first.getKey(), cat2 = second.getKey();
            double amt2 = second.getValue();
            int pct2 = (int) Math.round(amt2 / totalThisMonth * 100);
            return tpl.replace("${cat1}", cat1)
                    .replace("${cat2}", cat2)
                    .replace("${amt2}", String.format("%.2f", amt2))
                    .replace("${pct2}", String.valueOf(pct2));
        }
        if ("MONTHLY_TREND".equals(rule)) {
            double change = (thisMonthTotal - lastMonthTotal) / lastMonthTotal * 100;
            String verb = change < 0 ? "reduced" : "increased";
            String adj  = change < 0 ? "lower"   : "higher";
            return tpl.replace("${last}",  String.format("%.2f", lastMonthTotal))
                    .replace("${this}",  String.format("%.2f", thisMonthTotal))
                    .replace("${chgPct}", String.valueOf((int) Math.abs(Math.round(change))))
                    .replace("${trendVerb}", verb)
                    .replace("${trendAdj}",  adj);
        }
        if ("YTD_THRESHOLD".equals(rule)) {
            int pct = (int) Math.round(pctYtd);
            return tpl.replace("${ytd}",    String.format("%.2f", ytdExpense))
                    .replace("${pctYtd}", String.valueOf(pct));
        }
        // fallback
        return tpl;
    }
}

package com.example.expense.expense_tracker.service;

import com.example.expense.expense_tracker.expense_entity.Income;
import com.example.expense.expense_tracker.expense_entity.MonthlyDTO;
import com.example.expense.expense_tracker.expense_entity.YearlyIncomeDTO;
import com.example.expense.expense_tracker.repos.IncomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    private static final Map<String,Double> CATEGORY_YEARLY = Map.of(
            "<10k", 5000.0,
            "11k-20k", 15500.0,
            "21k-30k", 25500.0,
            "31k-40k", 35500.0,
            "41k-50k", 45500.0,
            "51k-60k", 55500.0,
            "61k-70k" , 65500.0,
            "71k-80k", 75500.0,
            "81k-90k", 85500.0,
            "91k-100k", 95500.0
    );

    public List<MonthlyDTO> findByDate(String start , String end){
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);

        return incomeRepository.findMonthlySummary(s, e);
    }

    public List<MonthlyDTO> fetchAll(){
        return incomeRepository.findMonthlySummary(LocalDate.of(2000, 1,1) , LocalDate.now());
    }

    public Income saveIncome(Income income){

        return incomeRepository.save(income);
    }

    @Transactional
    public Income updateIncome(Long id, Income updated){
        Income inc = incomeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("data not found " + id));
        inc.setAmount(updated.getAmount());
        inc.setSource(updated.getSource());
        inc.setDate(LocalDate.now());
        return inc;
    }

    @Transactional(readOnly = true)
    public MonthlyDTO getCurrentMonthIncome() {
        LocalDate today = LocalDate.now();
        LocalDate firstOfMonth = today.withDayOfMonth(1);
        LocalDate lastOfMonth = today.withDayOfMonth(today.lengthOfMonth());


        List<MonthlyDTO> results = incomeRepository.findMonthlySummary(firstOfMonth, lastOfMonth);
        if (!results.isEmpty()) {
            return results.get(0);
        }

        Optional<Income> latestInc = incomeRepository.findTopByOrderByDateDesc();
        if (latestInc.isPresent()) {
            Income inc = latestInc.get();
            return new MonthlyDTO(
                    inc.getDate().getYear(),
                    inc.getDate().getMonthValue(),
                    inc.getAmount()
            );
        }

        return new MonthlyDTO(
                today.getYear(),
                today.getMonthValue(),
                0.0
        );
    }

    @Transactional
    public List<MonthlyDTO> expandYearlyCategoryToMonthly(List<YearlyIncomeDTO> yearlyCategory){
        List<MonthlyDTO> result = new ArrayList<>();
        List<Income> toPersist = new ArrayList<>();

        for(YearlyIncomeDTO yc : yearlyCategory){
            Integer year = yc.getYear();
            String category = yc.getCategory();

            Double yearlyAmount = CATEGORY_YEARLY.get(category);
            if(yearlyAmount == null){
                throw new IllegalArgumentException("Unknown category: " + category);
            }

            double monthlyAmount = yearlyAmount / 12.0;
            String source = "CATEGORY_" + category;

            for(int month = 1; month <= 12 ; month++){
                MonthlyDTO dto = new MonthlyDTO(year,month,monthlyAmount);
                result.add(dto);

                LocalDate date = LocalDate.of(year,month, 1);
                Optional<Income> existInc = incomeRepository.findByDate(date);

                Income inc;
                if(existInc.isPresent()){
                    System.out.println("on database");
                    inc = existInc.get();
                    inc.setAmount(monthlyAmount);
                }else{
                    System.out.println("not on database");
                    inc = Income.builder()
                            .amount(monthlyAmount)
                            .source(source)
                            .date(date)
                            .build();
                }
                toPersist.add(inc);
            }
        }
        incomeRepository.saveAll(toPersist);
        return result;
    }

    public void deleteIncome(Long id){
        if (!incomeRepository.existsById(id)) {
            throw new IllegalArgumentException("Income not found: " + id);
        }
        incomeRepository.deleteById(id);
    }
}

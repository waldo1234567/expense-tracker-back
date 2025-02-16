-keep public class com.example.expense.expense_tracker.ExpenseTrackerApplication {
    public static void main(java.lang.String[]);
}

-keepattributes *Annotation*

-keepclassmembers class * {
    @org.springframework.beans.factory.annotation.Autowired *;
    @org.springframework.web.bind.annotation.RequestMapping *;
}

-keep class com.example.expense.** { *; }

-obfuscate
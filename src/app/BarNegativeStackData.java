package app;

import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;



public class BarNegativeStackData {
    private static CategoryModel model;
    static {
        model = new DefaultCategoryModel();
        model.setValue("Male", "0-4", -1746181);
        model.setValue("Male", "5-9", -1884428);
        model.setValue("Male", "10-14", -2089758);
        model.setValue("Male", "15-19", -2222362);
        model.setValue("Male", "20-24", -2537431);
        model.setValue("Male", "25-29", -2507081);
        model.setValue("Male", "30-34", -2443179);
        model.setValue("Male", "35-39", -2664537);
        model.setValue("Male", "40-44", -3556505);
        model.setValue("Male", "45-49", -3680231);
        model.setValue("Male", "50-54", -3143062);
        model.setValue("Male", "55-59", -2721122);
        model.setValue("Male", "60-64", -2229181);
        model.setValue("Male", "65-69", -2227768);
        model.setValue("Male", "70-74", -2176300);
        model.setValue("Male", "75-79", -1329968);
        model.setValue("Male", "80-84", -836804);
        model.setValue("Male", "85-89", -354784);
        model.setValue("Male", "90-94", -90569);
        model.setValue("Male", "95-99", -28367);
        model.setValue("Male", "100 +", -3878);
        model.setValue("Female", "0-4", 1656154);
        model.setValue("Female", "5-9", 1787564);
        model.setValue("Female", "10-14", 1981671);
        model.setValue("Female", "15-19", 2108575);
        model.setValue("Female", "20-24", 2403438);
        model.setValue("Female", "25-29", 2366003);
        model.setValue("Female", "30-34", 2301402);
        model.setValue("Female", "35-39", 2519874);
        model.setValue("Female", "40-44", 3360596);
        model.setValue("Female", "45-49", 3493473);
        model.setValue("Female", "50-54", 3050775);
        model.setValue("Female", "55-59", 2759560);
        model.setValue("Female", "60-64", 2304444);
        model.setValue("Female", "65-69", 2426504);
        model.setValue("Female", "70-74", 2568938);
        model.setValue("Female", "75-79", 1785638);
        model.setValue("Female", "80-84", 1447162);
        model.setValue("Female", "85-89", 1005011);
        model.setValue("Female", "90-94", 330870);
        model.setValue("Female", "95-99", 130632);
        model.setValue("Female", "100 +", 21208);
    }
    
    public static CategoryModel getCategoryModel() {
        return model;
    }
    
    public static String[] getCategories() {
        return model.getCategories().toArray(new String[0]);
    }
}
package com.hcmus.group14.moneytor.services.options;


public enum Category
{
    FOOD_AND_DRINK("Food & Drink"),     //Food and drink related
    TRAFFIC("Traffic"),                 //For vehicles, gasoline,...
    BILLS("Bills"),                     //Cyclical bills (electricity, water, internet, Netflix...)
    MAINTENANCE("Maintenance"),         //House/vehicle maintenance
    HEALTH("Heath"),                    //Healthcare (drugs 'n stuff)
    EDUCATION("Education"),             //Education and study related
    APPLIANCES("Appliances"),           //Household appliances (TV, fridge,...)
    PETS("Pets"),                       //Related to pets (if available), stuff like food, medical expenses...
    UTILITIES("Utilities"),             //Other stuff related to household (internet installment...)
    FITNESS("Fitness"),                 //Gym, aerobics, etc.
    MAKEUP("Makeup"),                   //Beauty expenses
    ENTERTAINMENT("Entertainment");     //For fun stuff

    private final String name;
    // TODO: image for each category
    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.toString();
    }
}

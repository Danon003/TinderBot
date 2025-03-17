package com.javarush.telegram;

public class UserInfo {
    private String name = null; //Имя
    private String sex; //Пол
    public String age; //Возраст
    public String city; //Город
    public String occupation; //Профессия
    public String hobby; //Хобби
    public String handsome; //Красота, привлекательность
    public String wealth; //Доход, богатство
    public String annoys; //Меня раздражает в людях
    public String goals; //Цели знакомства

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex(){
        return this.sex.toLowerCase();
    }
    public void setSex(String sex){
        this.sex = sex;
    }
    private String fieldToString(String str, String description) {
        if (str != null && !str.isEmpty())
            return description + ": " + str + "\n";
        else
            return "";
    }

    @Override
    public String toString() {
        String result = "";

        result += fieldToString(name, "Имя");
        result += fieldToString(sex, "Пол");
        result += fieldToString(age, "Возраст");
        result += fieldToString(city, "Город");
        result += fieldToString(occupation, "Профессия");
        result += fieldToString(hobby, "Хобби");
        result += fieldToString(handsome, "Красота, привлекательность в баллах (максимум 10 баллов)");
        result += fieldToString(wealth, "Доход, богатство");
        result += fieldToString(annoys, "В людях раздражает");
        result += fieldToString(goals, "Цели знакомства");

        return result;
    }
}

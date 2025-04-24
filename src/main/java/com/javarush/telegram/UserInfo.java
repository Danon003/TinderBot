package com.javarush.telegram;

public class UserInfo {
    private String name = null; //Имя
    private String sex; //Пол
    private String age; //Возраст
    private String city; //Город
    private String occupation; //Профессия
    private String hobby; //Хобби
    public String handsome; //Красота, привлекательность
    public String wealth; //Доход, богатство
    private String annoys; //Меня раздражает в людях
    private String goals; //Цели знакомства

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
        result += fieldToString(getAge(), "Возраст");
        result += fieldToString(getCity(), "Город");
        result += fieldToString(getOccupation(), "Профессия");
        result += fieldToString(getHobby(), "Хобби");
        result += fieldToString(handsome, "Красота, привлекательность в баллах (максимум 10 баллов)");
        result += fieldToString(wealth, "Доход, богатство");
        result += fieldToString(getAnnoys(), "В людях раздражает");
        result += fieldToString(getGoals(), "Цели знакомства");

        return result;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getAnnoys() {
        return annoys;
    }

    public void setAnnoys(String annoys) {
        this.annoys = annoys;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }
}

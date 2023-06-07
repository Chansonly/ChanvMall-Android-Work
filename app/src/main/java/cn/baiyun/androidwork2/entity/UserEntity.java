package cn.baiyun.androidwork2.entity;


public class UserEntity {
    private Integer id;
    private String name;
    private Integer gender;
    private Integer age;
    private String password;
    private String flavor;

    public UserEntity(Integer id, String name, Integer gender, Integer age, String password, String flavor) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.password = password;
        this.flavor = flavor;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", flavor='" + flavor + '\'' +
                '}';
    }

    public UserEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }
}

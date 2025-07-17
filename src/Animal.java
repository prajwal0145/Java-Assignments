public class Animal {
    int age;
    String gender;
    int WeightInlbs;

    public Animal(int age, String gender,int WeightInlbs ){
        this.age=age;
        this.gender=gender;
        this.WeightInlbs=WeightInlbs;
    }

    public void eat(){
        System.out.println("eating..");
    }

    public void run(){
        System.out.println("running..");
    }
}

import java.util.ArrayList;

public class Student {
    private String examSeatNo;
    private String name;
    private String sgpa;
    private ArrayList<Semester> semesterList = new ArrayList<>();
    private boolean reappeared = false;

    public Student(String examSeatNo, String name, String sgpa) {
        this.examSeatNo = examSeatNo;
        this.name = name;
        this.sgpa = sgpa;
    }
    public Student(String examSeatNo, String name, boolean reappeared) {
        this.examSeatNo = examSeatNo;
        this.name = name;
        this.reappeared = reappeared;
    }
    public Student(String examSeatNo, String name, String sgpa, boolean reappeared) {
        this.examSeatNo = examSeatNo;
        this.name = name;
        this.sgpa = sgpa;
        this.reappeared = reappeared;
    }

    public Student() {

    }

    public ArrayList<Semester> getSemesterList() {
        return semesterList;
    }

    public void setSemesterList(ArrayList<Semester> semesterList) {
        this.semesterList = semesterList;
    }

    public boolean isReappeared() {
        return reappeared;
    }

    public void setReappeared(boolean reappeared) {
        this.reappeared = reappeared;
    }

    public String getExamSeatNo() {
        return examSeatNo;
    }

    public void setExamSeatNo(String examSeatNo) {
        this.examSeatNo = examSeatNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSgpa() {
        return sgpa;
    }

    public void setSgpa(String sgpa) {
        this.sgpa = sgpa;
    }

    @Override
    public String toString() {
        return examSeatNo+"\t"+name+"\t"+sgpa+"\t"+reappeared;
    }
}

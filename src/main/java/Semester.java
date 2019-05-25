import java.util.ArrayList;

public class Semester {
    ArrayList<subject> SubjectList = new ArrayList<>();
    int semNo;
    String gpa;

    @Override
    public String toString() {
        return "Sem no: "+semNo;
    }

    public Semester() {
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public ArrayList<subject> getSubjectList() {
        return SubjectList;
    }

    public void setSubjectList(ArrayList<subject> subjectList) {
        SubjectList = subjectList;
    }

    public int getTotalNumberOfSubjects(){
        return SubjectList.size();
    }

    public boolean addSubject(subject subject) {
        return SubjectList.add(subject);
    }

    public int getSemNo() {
        return semNo;
    }

    public void setSemNo(int semNo) {
        this.semNo = semNo;
    }

    public Semester(int semNo) {
        this.semNo = semNo;
    }
}

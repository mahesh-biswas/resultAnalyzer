public class subject {
    String subject,Grd,TotP;
    int IN=-1,TH=-1,TW=-1,PR=-1,OR=-1,Crd=-1,GPts=-1,CPts=-1;
    int ipt=-1;
    public subject(String subject) {
        this.subject = subject;
    }

    public subject(String subject, String grd, int IN, int TH, int TW, int PR, int OR, String totP, int crd, int GPts, int CPts) {
        this.subject = subject;
        Grd = grd;
        this.IN = IN;
        this.TH = TH;
        this.TW = TW;
        this.PR = PR;
        this.OR = OR;
        TotP = totP;
        Crd = crd;
        this.GPts = GPts;
        this.CPts = CPts;
    }

    public int getIpt() {
        return getIN()+getTH();
    }

    public void setIpt(int ipt) {
        this.ipt = ipt;
    }

    public String getGrd() {
        return Grd;
    }

    public void setGrd(String grd) {
        Grd = grd;
    }

    public int getIN() {
        return IN;
    }

    public void setIN(int IN) {
        this.IN = IN;
    }

    public int getTH() {
        return TH;
    }

    public void setTH(int TH) {
        this.TH = TH;
    }

    public int getTW() {
        return TW;
    }

    public void setTW(int TW) {
        this.TW = TW;
    }

    public int getPR() {
        return PR;
    }

    public void setPR(int PR) {
        this.PR = PR;
    }

    public int getOR() {
        return OR;
    }

    public void setOR(int OR) {
        this.OR = OR;
    }

    public String getTotP() {
        return TotP;
    }

    public void setTotP(String totP) {
        TotP = totP;
    }

    public int getCrd() {
        return Crd;
    }

    public void setCrd(int crd) {
        Crd = crd;
    }

    public int getGPts() {
        return GPts;
    }

    public void setGPts(int GPts) {
        this.GPts = GPts;
    }

    public int getCPts() {
        return CPts;
    }

    public void setCPts(int CPts) {
        this.CPts = CPts;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "sub:"+subject+"\t IN: "+getIN()+
                " TH: "+getTH()+
                " [IN+TH]: "+add(getIN(),getTH())+
                " TW: "+getTW()+
                " PR: "+getPR()+
                " OR: "+getOR()+
                " Tot%: "+getTotP()+
                " Crd: "+getCrd()+
                " Grd: "+getGrd()+
                " GPts: "+getGPts()+
                " CPts: "+getCPts();
    }

    int add(int x,int y){
        if(x>=0 && y>=0){
            return x+y;
        }
        return -1;
    }
}

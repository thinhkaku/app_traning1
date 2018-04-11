package aero.pilotlog.databases.entities;

/**
 * Created by phuc.dd on 8/7/2017.
 */

public class Totals {

    private Integer totalsID;
    private Integer sumPIC;
    private Integer sumCoP;
    private Integer sumDual;
    private Integer sumPICus;
    private Integer sumInstr;
    private Integer sumExam;
    private Integer sumIFR;
    private Integer sumSFR;
    private Integer sumIMT;
    private Integer sumREL;
    private Integer sumNight;
    private Integer sumXC;
    private Integer sumU1;
    private Integer sumU2;
    private Integer sumU3;
    private Integer sumU4;
    private Integer sumClass6;
    private Integer sumClass1;
    private Integer sumClass2;
    private Integer sumClass3;
    private Integer sumClass4;
    private Integer sumClass5;
    private Integer sumPowerM1;
    private Integer sumPower0;
    private Integer sumPower1;
    private Integer sumPower2;
    private Integer sumPower3;
    private Integer sumPower4;
    private Integer sumPower5;
    private Integer sumPower6;
    private Integer sumTODay;
    private Integer sumTONight;
    private Integer sumLdgDay;
    private Integer sumLdgNight;
    private Integer sumHolding;
    private Integer sumLift;
    private Integer sumACFT;
    private Integer sumSIM;
    private Integer sumDRONE;

    public Totals() {
    }

    public Totals(Integer totalsID) {
        this.totalsID = totalsID;
    }

    public Totals(Integer totalsID, Integer sumPIC, Integer sumCoP, Integer sumDual, Integer sumPICus,
                  Integer sumInstr, Integer sumExam, Integer sumIFR, Integer sumSFR, Integer sumIMT,
                  Integer sumREL, Integer sumNight, Integer sumXC, Integer sumU1, Integer sumU2,
                  Integer sumU3, Integer sumU4, Integer sumClass1, Integer sumClass2, Integer sumClass3,
                  Integer sumClass4, Integer sumClass5, Integer sumClass6, Integer sumPower0, Integer sumPower1, Integer sumPower2,
                  Integer sumPower3, Integer sumPower4, Integer sumPower5, Integer sumPower6, Integer sumTODay,
                  Integer sumTONight, Integer sumLdgDay, Integer sumLdgNight, Integer sumHolding, Integer sumLift,
                  Integer sumACFT, Integer sumSIM, Integer sumDRONE) {
        this.totalsID = totalsID;
        this.sumPIC = sumPIC;
        this.sumCoP = sumCoP;
        this.sumDual = sumDual;
        this.sumPICus = sumPICus;
        this.sumInstr = sumInstr;
        this.sumExam = sumExam;
        this.sumIFR = sumIFR;
        this.sumSFR = sumSFR;
        this.sumIMT = sumIMT;
        this.sumREL = sumREL;
        this.sumNight = sumNight;
        this.sumXC = sumXC;
        this.sumU1 = sumU1;
        this.sumU2 = sumU2;
        this.sumU3 = sumU3;
        this.sumU4 = sumU4;
        this.sumClass6 = sumClass6;
        this.sumClass1 = sumClass1;
        this.sumClass2 = sumClass2;
        this.sumClass3 = sumClass3;
        this.sumClass4 = sumClass4;
        this.sumClass5 = sumClass5;
        this.sumPowerM1 = 0;
        this.sumPower0 = sumPower0;
        this.sumPower1 = sumPower1;
        this.sumPower2 = sumPower2;
        this.sumPower3 = sumPower3;
        this.sumPower4 = sumPower4;
        this.sumPower5 = sumPower5;
        this.sumPower6 = sumPower6;
        this.sumTODay = sumTODay;
        this.sumTONight = sumTONight;
        this.sumLdgDay = sumLdgDay;
        this.sumLdgNight = sumLdgNight;
        this.sumHolding = sumHolding;
        this.sumLift = sumLift;
        this.sumACFT = sumACFT;
        this.sumSIM = sumSIM;
        this.sumDRONE = sumDRONE;
    }

    public Integer getTotalsID() {
        return totalsID;
    }

    public void setTotalsID(Integer totalsID) {
        this.totalsID = totalsID;
    }

    public Integer getSumPIC() {
        return sumPIC;
    }

    public void setSumPIC(Integer sumPIC) {
        this.sumPIC = sumPIC;
    }

    public Integer getSumCoP() {
        return sumCoP;
    }

    public void setSumCoP(Integer sumCoP) {
        this.sumCoP = sumCoP;
    }

    public Integer getSumDual() {
        return sumDual;
    }

    public void setSumDual(Integer sumDual) {
        this.sumDual = sumDual;
    }

    public Integer getSumPICus() {
        return sumPICus;
    }

    public void setSumPICus(Integer sumPICus) {
        this.sumPICus = sumPICus;
    }

    public Integer getSumInstr() {
        return sumInstr;
    }

    public void setSumInstr(Integer sumInstr) {
        this.sumInstr = sumInstr;
    }

    public Integer getSumExam() {
        return sumExam;
    }

    public void setSumExam(Integer sumExam) {
        this.sumExam = sumExam;
    }

    public Integer getSumIFR() {
        return sumIFR;
    }

    public void setSumIFR(Integer sumIFR) {
        this.sumIFR = sumIFR;
    }

    public Integer getSumSFR() {
        return sumSFR;
    }

    public void setSumSFR(Integer sumSFR) {
        this.sumSFR = sumSFR;
    }

    public Integer getSumIMT() {
        return sumIMT;
    }

    public void setSumIMT(Integer sumIMT) {
        this.sumIMT = sumIMT;
    }

    public Integer getSumREL() {
        return sumREL;
    }

    public void setSumREL(Integer sumREL) {
        this.sumREL = sumREL;
    }

    public Integer getSumNight() {
        return sumNight;
    }

    public void setSumNight(Integer sumNight) {
        this.sumNight = sumNight;
    }

    public Integer getSumXC() {
        return sumXC;
    }

    public void setSumXC(Integer sumXC) {
        this.sumXC = sumXC;
    }

    public Integer getSumU1() {
        return sumU1;
    }

    public void setSumU1(Integer sumU1) {
        this.sumU1 = sumU1;
    }

    public Integer getSumU2() {
        return sumU2;
    }

    public void setSumU2(Integer sumU2) {
        this.sumU2 = sumU2;
    }

    public Integer getSumU3() {
        return sumU3;
    }

    public void setSumU3(Integer sumU3) {
        this.sumU3 = sumU3;
    }

    public Integer getSumU4() {
        return sumU4;
    }

    public void setSumU4(Integer sumU4) {
        this.sumU4 = sumU4;
    }

    public Integer getSumClass1() {
        return sumClass1;
    }

    public void setSumClass1(Integer sumClass1) {
        this.sumClass1 = sumClass1;
    }

    public Integer getSumClass2() {
        return sumClass2;
    }

    public void setSumClass2(Integer sumClass2) {
        this.sumClass2 = sumClass2;
    }

    public Integer getSumClass3() {
        return sumClass3;
    }

    public void setSumClass3(Integer sumClass3) {
        this.sumClass3 = sumClass3;
    }

    public Integer getSumClass4() {
        return sumClass4;
    }

    public void setSumClass4(Integer sumClass4) {
        this.sumClass4 = sumClass4;
    }

    public Integer getSumClass5() {
        return sumClass5;
    }

    public void setSumClass5(Integer sumClass5) {
        this.sumClass5 = sumClass5;
    }

    public Integer getSumPower0() {
        return sumPower0;
    }

    public void setSumPower0(Integer sumPower0) {
        this.sumPower0 = sumPower0;
    }

    public Integer getSumPower1() {
        return sumPower1;
    }

    public void setSumPower1(Integer sumPower1) {
        this.sumPower1 = sumPower1;
    }

    public Integer getSumPower2() {
        return sumPower2;
    }

    public void setSumPower2(Integer sumPower2) {
        this.sumPower2 = sumPower2;
    }

    public Integer getSumPower3() {
        return sumPower3;
    }

    public void setSumPower3(Integer sumPower3) {
        this.sumPower3 = sumPower3;
    }

    public Integer getSumPower4() {
        return sumPower4;
    }

    public void setSumPower4(Integer sumPower4) {
        this.sumPower4 = sumPower4;
    }

    public Integer getSumPower5() {
        return sumPower5;
    }

    public void setSumPower5(Integer sumPower5) {
        this.sumPower5 = sumPower5;
    }

    public Integer getSumPower6() {
        return sumPower6;
    }

    public void setSumPower6(Integer sumPower6) {
        this.sumPower6 = sumPower6;
    }

    public Integer getSumTODay() {
        return sumTODay;
    }

    public void setSumTODay(Integer sumTODay) {
        this.sumTODay = sumTODay;
    }

    public Integer getSumTONight() {
        return sumTONight;
    }

    public void setSumTONight(Integer sumTONight) {
        this.sumTONight = sumTONight;
    }

    public Integer getSumLdgDay() {
        return sumLdgDay;
    }

    public void setSumLdgDay(Integer sumLdgDay) {
        this.sumLdgDay = sumLdgDay;
    }

    public Integer getSumLdgNight() {
        return sumLdgNight;
    }

    public void setSumLdgNight(Integer sumLdgNight) {
        this.sumLdgNight = sumLdgNight;
    }

    public Integer getSumHolding() {
        return sumHolding;
    }

    public void setSumHolding(Integer sumHolding) {
        this.sumHolding = sumHolding;
    }

    public Integer getSumACFT() {
        return sumACFT;
    }

    public void setSumACFT(Integer sumACFT) {
        this.sumACFT = sumACFT;
    }

    public Integer getSumSIM() {
        return sumSIM;
    }

    public void setSumSIM(Integer sumSIM) {
        this.sumSIM = sumSIM;
    }

    public Integer getSumClass6() {
        return sumClass6;
    }

    public void setSumClassM1(Integer sumClass6) {
        this.sumClass6 = sumClass6;
    }

    public Integer getSumPowerM1() {
        return sumPowerM1;
    }

    public void setSumPowerM1(Integer sumPowerM1) {
        this.sumPowerM1 = sumPowerM1;
    }

    public Integer getSumDRONE() {
        return sumDRONE;
    }

    public void setSumDRONE(Integer sumDRONE) {
        this.sumDRONE = sumDRONE;
    }

    public Integer getSumLift() {
        return sumLift;
    }

    public void setSumLift(Integer sumLift) {
        this.sumLift = sumLift;
    }
}


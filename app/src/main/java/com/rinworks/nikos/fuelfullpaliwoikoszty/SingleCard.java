package com.rinworks.nikos.fuelfullpaliwoikoszty;

public class SingleCard {
    private String vZatankowano;
    private String vZaplacono;
    private String vPrzejechano;
    private String vSpalanie;

    public SingleCard() {
        //todo: Baza danych IMO?
        vZatankowano = "36.6L";
        vZaplacono = "182.64 ZŁ";
        vPrzejechano = "456.7 KM";
        vSpalanie = "6.7L/100";
    }

    public String getvZatankowano() {
        return vZatankowano;
    }

    public void setvZatankowano(String vZatankowano) {
        this.vZatankowano = vZatankowano;
    }

    public String getvZaplacono() {
        return vZaplacono;
    }

    public void setvZaplacono(String vZaplacono) {
        this.vZaplacono = vZaplacono;
    }

    public String getvPrzejechano() {
        return vPrzejechano;
    }

    public void setvPrzejechano(String vPrzejechano) {
        this.vPrzejechano = vPrzejechano;
    }

    public String getvSpalanie() {
        return vSpalanie;
    }

    public void setvSpalanie(String vSpalanie) {
        this.vSpalanie = vSpalanie;
    }
}

package Deck;

public class Card {
    private String stringValue; // A readable value of a card (i.e. 8H for 8 of hearts)
    private int numericalValue; // A numerical value representing a type of card (i.e. 47 for 8 of hearts)

    Card(String stringValue, int numericalValue){
        this.stringValue = stringValue;
        this.numericalValue = numericalValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getNumericalValue() {
        return numericalValue;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Card){
            Card card = (Card)obj;
            return card.getStringValue().equals(stringValue) &&
                    card.getNumericalValue() == numericalValue;
        }
        else return false;
    }

    @Override
    public String toString(){
        return stringValue;
    }
}

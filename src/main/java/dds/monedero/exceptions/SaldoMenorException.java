package dds.monedero.exceptions;

public class SaldoMenorException extends RuntimeException {
  public SaldoMenorException(String message) {
    super(message);
  }
}

//Todas las excepciones son el code Smell Duplicated Code
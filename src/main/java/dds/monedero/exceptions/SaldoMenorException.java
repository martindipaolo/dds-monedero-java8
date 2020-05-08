package dds.monedero.exceptions;

public class SaldoMenorException extends RuntimeException {
  public SaldoMenorException(double saldo) {
    super("No puede extraer mas de " + saldo + " $");
  }
}
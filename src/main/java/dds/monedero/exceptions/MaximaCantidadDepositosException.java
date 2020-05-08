package dds.monedero.exceptions;

public class MaximaCantidadDepositosException extends RuntimeException {

  public MaximaCantidadDepositosException(int maximoDepositosDiarios) {
    super("Ya excedio los " + maximoDepositosDiarios + " depositos diarios");
  }

}
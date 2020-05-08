package dds.monedero.exceptions;

public class MaximoExtraccionDiarioException extends RuntimeException {
  public MaximoExtraccionDiarioException(double limiteExtraccionDiario, double limite) {
    super("No puede extraer mas de $ " + limiteExtraccionDiario + " diarios, límite: " + limite);
  }
}
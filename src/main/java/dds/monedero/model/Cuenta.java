package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();
  private int maximoDepositosDiarios = 3;
  private double limiteExtraccionDiario = 1000;

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }


  public void depositar(double monto) {
    validarDeposito(monto);
    this.saldo += monto;
    this.agregarMovimiento(new Movimiento(LocalDate.now(), monto, true));
  }

  public void extraer(double monto) {
    validarExtraccion(monto);
    this.saldo -= monto;
    this.agregarMovimiento(new Movimiento(LocalDate.now(), monto, false));
  }


  public void agregarMovimiento(Movimiento movimiento) {
      movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  private void validarDeposito(double monto) {
    validarMontoPositivo(monto);
    validarLimiteDepositosDiariosExcedido();
  }



  private void validarExtraccion(double monto) {
    validarMontoPositivo(monto);

    validarSaldoSuficiente(monto);

    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = limiteExtraccionDiario - montoExtraidoHoy;

    if (monto > limite) {
      throw new MaximoExtraccionDiarioException(this.limiteExtraccionDiario, limite);
    }
  }

  private void validarSaldoSuficiente(double monto) {
    if (getSaldo() - monto < 0) {
      throw new SaldoMenorException(getSaldo());
    }
  }

  private void validarMontoPositivo(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto);
    }
  }

  private void validarLimiteDepositosDiariosExcedido() {
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= this.maximoDepositosDiarios) {
      throw new MaximaCantidadDepositosException(this.maximoDepositosDiarios);
    }
  }

}

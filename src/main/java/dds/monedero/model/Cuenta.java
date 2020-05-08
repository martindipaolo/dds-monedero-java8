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
    if (monto <= 0) {
      throw new MontoNegativoException(monto);
    }

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= this.maximoDepositosDiarios) {
      throw new MaximaCantidadDepositosException(this.maximoDepositosDiarios);
    }

    this.saldo += monto;
    this.agregarMovimiento(LocalDate.now(), monto, true);
  }

  public void extraer(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto);
    }
    if (getSaldo() - monto < 0) {
      throw new SaldoMenorException(getSaldo());
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = limiteExtraccionDiario - montoExtraidoHoy;
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException(this.limiteExtraccionDiario, limite);
    }
    this.saldo -= monto;
    this.agregarMovimiento(LocalDate.now(), monto, false);
  }

  public void agregarMovimiento(LocalDate fecha, double monto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, monto, esDeposito);
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

}

package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }//borrar?

  void validarYCrearMovimiento(double cuanto,Movimiento movimiento,Runnable validaciones){
    validarMontoPositivo(cuanto);
    validaciones.run();
    agregarMovimiento(movimiento);
  }
  public void poner(double cuanto) {
    validarYCrearMovimiento(cuanto,new Deposito(LocalDate.now(), cuanto),()->{validarDepositosMenoresA3();});
  }

  public void sacar(double cuanto) {
    validarYCrearMovimiento(cuanto,new Extraccion(LocalDate.now(), cuanto),()->{
      validarPosibilidadDeSacar(cuanto);
      validarExcesoAlSacar(cuanto);});
  }

  public void agregarMovimiento(Movimiento movimiento) {
    setSaldo(movimiento.calcularValor(this));
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
  private void validarDepositosMenoresA3() {
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  private void validarMontoPositivo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  private void validarExcesoAlSacar(double cuanto) {
    double limite = 1000 - getMontoExtraidoA(LocalDate.now());
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }
  private void validarPosibilidadDeSacar(double cuanto) {
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

}

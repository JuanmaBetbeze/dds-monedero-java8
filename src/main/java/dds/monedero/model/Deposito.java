package dds.monedero.model;

import java.time.LocalDate;

public class Deposito extends Movimiento{
    public Deposito(LocalDate fecha, double monto) {
        super(fecha, monto);
    }


    public boolean fueExtraido(LocalDate fecha) {
      return false;
    }

    @Override
    public double calcularValor(Cuenta cuenta) {
        return cuenta.getSaldo() + getMonto();
    }

    @Override
    boolean isDeposito() {
        return true;
    }
}

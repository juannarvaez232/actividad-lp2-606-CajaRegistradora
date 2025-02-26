import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class FrmCajaRegistradora extends JFrame {

    JTextArea txtPreguntaRegistro;
    JComboBox<String> cmbDenom;
    JTextField txtCantidad;
    JButton btnAgregar;
    JList<String> lstRegistros;

   
    JLabel lblMonto;
    JLabel lblDenominacion;
    JTextField txtMonto;
    JButton btnCalcular;
    JTable tblDevuelta;

    DefaultTableModel dtm;

    int[] registroDenom = new int[1000];
    int[] registroCant = new int[1000];
    int totalRegistros = -1;


    int[] denominaciones = {20000, 10000, 2000, 1000, 500, 200, 50};
    HashMap<Integer, String> tipoDenom = new HashMap<>();

    public FrmCajaRegistradora() {
        setSize(600, 600);
        setTitle("Caja registradora");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

      
        tipoDenom.put(20000, "Billete");
        tipoDenom.put(10000, "Billete");
        tipoDenom.put(2000, "Billete");
        tipoDenom.put(1000, "Billete");
        tipoDenom.put(500, "Moneda");
        tipoDenom.put(200, "Moneda");
        tipoDenom.put(50, "Moneda");

        
        

        String[] opciones = {"20000", "10000", "2000", "1000", "500", "200", "50"};
        cmbDenom = new JComboBox<>(opciones);
        cmbDenom.setBounds(300, 40, 100, 25);
        getContentPane().add(cmbDenom);

         lblDenominacion = new JLabel("Denominacion:");
        lblDenominacion.setBounds(200, 40, 200, 25);
        getContentPane().add(lblDenominacion);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(200, 110, 60, 25);
        getContentPane().add(txtCantidad);

        btnAgregar = new JButton("Actualizar Existencia:");
        btnAgregar.setBounds(10, 110, 180, 25);
        getContentPane().add(btnAgregar);
       

        
        lblMonto = new JLabel("Ingrese el valor a devolver:");
        lblMonto.setBounds(50, 200, 200, 25);
        getContentPane().add(lblMonto);

        txtMonto = new JTextField();
        txtMonto.setBounds(220, 200, 100, 25);
        getContentPane().add(txtMonto);

        btnCalcular = new JButton("Devolver");
        btnCalcular.setBounds(330, 200, 100, 25);
        getContentPane().add(btnCalcular);

        tblDevuelta = new JTable();
        dtm = new DefaultTableModel(null, new String[]{"Denominación", "Tipo", "Cantidad"});
        tblDevuelta.setModel(dtm);
        JScrollPane spDevuelta = new JScrollPane(tblDevuelta);
        spDevuelta.setBounds(10, 240, 550, 300);
        getContentPane().add(spDevuelta);

        
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                agregarRegistro();
            }
        });

        btnCalcular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                calcularDevuelta();
            }
        });
    }

    private void agregarRegistro() {
        try {
            int denom = Integer.parseInt(cmbDenom.getSelectedItem().toString());
            int cant = Integer.parseInt(txtCantidad.getText());
            totalRegistros++;
            registroDenom[totalRegistros] = denom;
            registroCant[totalRegistros] = cant;
            mostrarRegistros();
            txtCantidad.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.");
        }
    }

    private void mostrarRegistros() {
        String[] registrosStr = new String[totalRegistros + 1];
        for (int i = 0; i <= totalRegistros; i++) {
            registrosStr[i] = "$" + registroDenom[i] + " - Cantidad: " + registroCant[i];
        }
        lstRegistros.setListData(registrosStr);
    }

    private void calcularDevuelta() {
        dtm.setRowCount(0);
        try {
            int monto = Integer.parseInt(txtMonto.getText());

            
            int[] existencia = new int[denominaciones.length];
            for (int i = 0; i < denominaciones.length; i++) {
                existencia[i] = 0;
            }
            for (int i = 0; i <= totalRegistros; i++) {
                for (int j = 0; j < denominaciones.length; j++) {
                    if (registroDenom[i] == denominaciones[j]) {
                        existencia[j] += registroCant[i];
                        break;
                    }
                }
            }

            int montoPendiente = monto;
            for (int i = 0; i < denominaciones.length; i++) {
                int denom = denominaciones[i];
                int disponible = existencia[i];
                int cantidadNecesaria = montoPendiente / denom;
                int cantidadUsar;
                if (cantidadNecesaria > disponible) {
                    cantidadUsar = disponible;
                } else {
                    cantidadUsar = cantidadNecesaria;
                }
                if (cantidadUsar > 0) {
                    dtm.addRow(new Object[]{"$" + denom, tipoDenom.get(denom), cantidadUsar});
                    montoPendiente -= cantidadUsar * denom;
                }
            }
            if (montoPendiente != 0) {
                JOptionPane.showMessageDialog(this, "No se puede dar la devuelta exacta con la existencia disponible.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido.");
        }
    }
}



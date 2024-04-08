package mx.uv.fiee.iinf.poo.mytextfield;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Entry {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Agenda Teléfonica");
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel formulario = new JPanel();
        formulario.setLayout(new GridLayout(2, 2, 5, 5));
        JLabel nombre = new JLabel("Nombre: ");
        JTextField txtNombre = new JTextField(20);
        JLabel telefono = new JLabel("Teléfono: ");
        MyTextField my = new MyTextField ();
        formulario.add(nombre);
        formulario.add(txtNombre);
        formulario.add(telefono);
        formulario.add(my);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton anadir = new JButton("Add");
        panelBoton.add(anadir);

        JPanel panelTabla = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nombre");
        model.addColumn("Teléfono");

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setGridColor (Color.LIGHT_GRAY);
        table.setShowHorizontalLines (true);
        table.setShowVerticalLines (true);
        table.setDefaultEditor (Object.class, null);
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton save = new JButton("Save");
        JButton delete = new JButton("Delete");
        panelBotones2.add(save);
        panelBotones2.add(delete);

        // Agregar los paneles al panel principal
        mainPanel.add(formulario);
        mainPanel.add(panelBoton);
        mainPanel.add(panelTabla);
        mainPanel.add(panelBotones2);

        ArrayList<Contacto> contactos = cargarContactos();

        if (contactos != null) {
            for (Contacto contacto : contactos) {
                model.addRow(new Object[]{contacto.getNombre(), contacto.getTelefono()});
            }
        }

        anadir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (my.getText().isEmpty() || txtNombre.getText().isEmpty()) return;
                if (!my.getText().matches("\\(\\d{3}\\) \\d{7}")) {
                    JOptionPane.showMessageDialog(frame, "El formato del número de teléfono es incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Contacto nuevoContacto = new Contacto(txtNombre.getText(), my.getText());
                contactos.add(nuevoContacto);
                model.addRow(new Object[]{txtNombre.getText(), my.getText()});
                my.setText("");
                txtNombre.setText("");
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarContactos(contactos);
                JOptionPane.showMessageDialog(frame, "Los contactos se han guardado correctamente", "Guardado exitoso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow () > -1) {
                    contactos.remove(table.getSelectedRow ());
                    model.removeRow (table.getSelectedRow ());
                }
            }
        });

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

    }
    private static ArrayList<Contacto> cargarContactos() {
        ArrayList<Contacto> contactos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Agenda.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split("-");
                for (String parte : partes) {
                    String[] datos = parte.split(":");
                    String nombre = datos[0];
                    String telefono = datos[1];
                    contactos.add(new Contacto(nombre, telefono));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contactos;
    }
    private static void guardarContactos(ArrayList<Contacto> contactos) {
        try (FileOutputStream fos = new FileOutputStream("Agenda.txt");
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            for (Contacto contacto : contactos) {
                bw.write(contacto.getNombre() + ":" + contacto.getTelefono() + "-");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


class Contacto {
    private String nombre;
    private String telefono;

    public Contacto(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return this.nombre + ": " + this.telefono;
    }
}

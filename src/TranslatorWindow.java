import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//class for graphics
//contains the main class
public class TranslatorWindow extends JPanel {

    public static void main(String[] args) {

        boolean english = true;

        String typeOf = "EN-SP";
        Translator translator = new Translator();

        //Style, size of letters is chosen
        Font textFont = new Font("Arial", Font.PLAIN, 25);
        Font menuFont = new Font("Arial", Font.PLAIN, 25);

        //name of a frame
        JFrame frame = new JFrame("English - Spanish both-way translator");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();


        //text field are created
        JTextArea textField = new JTextArea();
        JTextArea output = new JTextArea();

        //output can not be edited
        output.setEditable(false);

        DefaultCaret caretTextField = (DefaultCaret) textField.getCaret();
        caretTextField.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        DefaultCaret caretOutput = (DefaultCaret) textField.getCaret();
        caretOutput.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);


        textField.setFont(textFont);
        output.setFont(textFont);


        panel1.add(textField, BorderLayout.WEST);
        panel2.add(output, BorderLayout.WEST);


        JScrollPane sp1 = new JScrollPane(textField);
        sp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JScrollPane sp2 = new JScrollPane(output);
        sp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        //size of text fields is made
        sp1.setPreferredSize(new Dimension(400, 150));
        sp2.setPreferredSize(new Dimension(400, 150));

        sp1.getVerticalScrollBar().setUnitIncrement(5);
        sp2.getVerticalScrollBar().setUnitIncrement(5);


        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);

        output.setLineWrap(true);
        output.setWrapStyleWord(true);

        panel1.add(sp1, BorderLayout.EAST);

        panel2.add(sp2, BorderLayout.EAST);


        //buttons are created
        //buttons define actions
        JButton okButton = new JButton("Translate");
        JButton changeLanguage = new JButton("English - Spanish");

        okButton.setPreferredSize(new Dimension(300, 90));
        changeLanguage.setPreferredSize(new Dimension(300, 90));

        okButton.setFont(menuFont);
        changeLanguage.setFont(menuFont);


        panel3.add(okButton, BorderLayout.WEST);
        panel3.add(changeLanguage, BorderLayout.EAST);

        frame.add(panel1, BorderLayout.WEST);
        frame.add(panel2, BorderLayout.EAST);
        frame.add(panel3, BorderLayout.PAGE_END);


        frame.pack();

        //size of the frame
        frame.setSize(830, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        //translated text is written when a button is pressed
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String text = textField.getText();
                text = text.replaceAll("[\r\n]+", " ");

                output.setText(translator.getTranslatedText(text, translator.getTypeOf()));
            }
        });


        //language change button functionality
        changeLanguage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (changeLanguage.getText().equals("English - Spanish")) {
                    changeLanguage.setText("Spanish - English");
                    translator.setTypeOf("SP-EN");
                } else if (changeLanguage.getText().equals("Spanish - English")) {
                    changeLanguage.setText("English - Spanish");
                    translator.setTypeOf("EN-SP");
                }

            }
        });

    }


}
package uebung3;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Options extends JFrame
{
    private DrawingPanel drawingPanel;

    public Options(DrawingPanel drawingPanel)
    {

        super("Options");
        this.drawingPanel = drawingPanel;
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocation(1010, 20);

        add(new Optionpanel());

        pack();
        setVisible(false);
    }

    class Optionpanel extends JPanel
    {
        public Optionpanel()
        {
            super();
            setBorder(BorderFactory.createTitledBorder("Options"));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JRadioButton naturalJRadioButton = new JRadioButton("natural", true);
            naturalJRadioButton.addItemListener(e ->
            {
                drawingPanel.setNatural(naturalJRadioButton.isSelected());
            });

            JRadioButton parabolicJRadioButton = new JRadioButton("parabolic");
            parabolicJRadioButton.addItemListener(e ->
            {
                drawingPanel.setParabolic(parabolicJRadioButton.isSelected());
            });

            JRadioButton clampedJRadioButton = new JRadioButton("clamped");
            clampedJRadioButton.addItemListener(e ->
            {
                drawingPanel.setClamped(clampedJRadioButton.isSelected());
            });

            JRadioButton closedJRadioButton = new JRadioButton("closed");
            closedJRadioButton.addItemListener(e ->
            {
                drawingPanel.setClosed(closedJRadioButton.isSelected());
            });

            ButtonGroup radioGroup = new ButtonGroup();
            radioGroup.add(naturalJRadioButton);
            radioGroup.add(parabolicJRadioButton);
            radioGroup.add(clampedJRadioButton);
            radioGroup.add(closedJRadioButton);

            JCheckBox bezierCheckBox = new JCheckBox("Show Bezier-Curve");
            bezierCheckBox.addItemListener(e ->
            {
                drawingPanel.setShow_bezier(bezierCheckBox.isSelected());
            });

            add(naturalJRadioButton);
            add(parabolicJRadioButton);
            add(clampedJRadioButton);
            add(closedJRadioButton);

            add(bezierCheckBox);

        }
    }

}

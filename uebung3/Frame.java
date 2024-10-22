package uebung3;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Frame extends JFrame
{

    private DrawingPanel drawingPanel;

    public Frame()
    {

        super("Splines");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.drawingPanel = new DrawingPanel();

        JMenuBar menubar = new JMenuBar();

        JMenuItem item_options = new JMenuItem("Options");

        Options frame_options = new Options(drawingPanel);

        item_options.addActionListener(e ->
        {

            frame_options.setVisible(true);
        });

        menubar.add(item_options);

        setJMenuBar(menubar);

        add(drawingPanel);

        pack();
        setVisible(true);
    }

    public static void main(String[] args)
    {
        new Frame();
    }

}

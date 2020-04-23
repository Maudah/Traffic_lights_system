
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;




public class MyActionListener implements ActionListener
{
    Event64 evToShabat,evToChol,evRegel;

    public MyActionListener(Event64 evToShabat, Event64 evToChol, Event64 evRegel) {

        this.evToShabat = evToShabat;
        this.evToChol = evToChol;
        this.evRegel = evRegel;
    }

    public void actionPerformed(ActionEvent e)
    {
        JRadioButton butt=(JRadioButton)e.getSource();
        if(butt.getName().equals("16"))
        {
            if(butt.isSelected())
                evToShabat.sendEvent();
            else
                evToChol.sendEvent();
        }
        else
            evRegel.sendEvent(butt.getName());
        System.out.println(butt.getName());
        		butt.setEnabled(false);
        		butt.setSelected(false);
    }

}

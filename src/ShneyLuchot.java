import java.awt.Color;

import javax.annotation.Generated;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPanel;


class ShneyLuchot extends Thread
{
    Ramzor ramzor;
    JPanel panel;
    Event64 evToRed,evToGreen,evAtRed,evToShabat,evToChol;

    enum OutState {ON_CHOL,ON_SHABAT};
    OutState outState;

    enum InState {ON_RED,ON_GREEN};
    InState inState;


    public ShneyLuchot(Ramzor ramzor, JPanel panel, Event64 evToGreen, Event64 evToRed, Event64 evAtRed,
                       Event64 evToShabat, Event64 evToChol) {

        this.ramzor = ramzor;
        this.panel = panel;
        this.evToRed = evToRed;
        this.evToGreen = evToGreen;
        this.evAtRed = evAtRed;
        this.evToShabat = evToShabat;
        this.evToChol = evToChol;
        start();
    }

    public ShneyLuchot( Ramzor ramzor,JPanel panel)
    {
        this.ramzor=ramzor;
        this.panel=panel;
        start();
    }

    public void run()
    {
        outState=OutState.ON_CHOL;
        inState=InState.ON_RED;
        setRed();
        while (true)
        {
            switch (outState) {
                case ON_CHOL:
                    while (outState==OutState.ON_CHOL)
                    {
                        switch (inState) {
                            case ON_RED:
                                while(true)
                                {
                                    if(evToGreen.arrivedEvent())
                                    {
                                        evToGreen.waitEvent();
                                        setGreen();
                                        inState=InState.ON_GREEN;
                                        break;
                                    }
                                    else if(evToShabat.arrivedEvent())
                                    {
                                        evToShabat.waitEvent();
                                        setOff();
                                        outState=OutState.ON_SHABAT;
                                        break;
                                    }
                                    else
                                        yield();
                                }
                                break;
                            case ON_GREEN:
                                while(true)
                                {
                                    if(evToRed.arrivedEvent())
                                    {
                                        evToRed.waitEvent();
                                        setRed();
                                        evAtRed.sendEvent();                                    
                                        inState=InState.ON_RED;
                                        break;
                                    }
                                    else if(evToShabat.arrivedEvent())
                                    {
                                        evToShabat.waitEvent();
                                        setOff();
                                        outState=OutState.ON_SHABAT;
                                        break;
                                    }
                                    else
                                        yield();
                                }
                                break;

                            default:
                                break;
                        }

                    }
                    break;
                case ON_SHABAT:
                    setOff();
                    while(true) {
                        if(evToChol.arrivedEvent()) {
                            evToChol.waitEvent();
                            setRed();
                            outState = OutState.ON_CHOL;
                            inState = InState.ON_RED;
                            break;
                        }
                        else
                            yield();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setOff() {
        setLight(1,Color.GRAY);
        setLight(2,Color.GRAY);
    }

    private void setGreen() {
        setLight(1,Color.GRAY);
        setLight(2,Color.GREEN);
    }
    private void setRed() {
        setLight(1,Color.RED);
        setLight(2,Color.GRAY);
    }

    public void setLight(int place, Color color)
    {
        ramzor.colorLight[place-1]=color;
        panel.repaint();
    }
}

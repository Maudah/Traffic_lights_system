
import java.awt.Color;

import javax.swing.JPanel;


class Echad extends Thread
{
    Ramzor ramzor;
    JPanel panel;
    Event64 evOff,evOn;

    enum States{ON_ON,ON_OFF};
    States states;

    public Echad( Ramzor ramzor,JPanel panel)
    {
        this.ramzor=ramzor;
        this.panel=panel;
        start();
    }

    public void run()
    {
    	MyTimer72 timer;
		Event64   evTimer;
		evTimer=new Event64();
		timer=  new MyTimer72(500,evTimer);
        states=States.ON_OFF;
        setOff();
        while (true)

            {
                switch (states) {
                    case ON_OFF:
                    	while(true) {
							if (evTimer.arrivedEvent()){                                     
								evTimer.waitEvent();
								break;
							}
							yield();
						}
                        setOn();
                        evTimer=new Event64();
                		timer=  new MyTimer72(500,evTimer);
                        states=States.ON_ON;                 
                    break;
                    case ON_ON:
                    	while(true) {
							if (evTimer.arrivedEvent()){                                     
								evTimer.waitEvent();
								break;
							}
							yield();
						}
                        setOff();
                        evTimer=new Event64();
                		timer=  new MyTimer72(500,evTimer);
                        states=States.ON_OFF;                    
                    break;
                    default:
                        break;
                }

            }      
    }

    private void setOn() {
        setLight(1,Color.YELLOW);
    }

    private void setOff() {
        setLight(1,Color.GRAY);
    }
    public void setLight(int place, Color color)
    {
        ramzor.colorLight[place-1]=color;
        panel.repaint();
    }
}

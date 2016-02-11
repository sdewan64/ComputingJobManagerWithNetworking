/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingjobmanager;

/**
 *
 * @author jubayer
 */
public class Machine {
    private int id;
    private boolean busy;
    private long until;

    public Machine(int id, boolean busy, long until) {
        this.id = id;
        this.busy = busy;
        this.until = until;
    }

    public int getId() {
        return id;
    }

    public boolean isBusy() {
        return busy;
    }

    public long getUntil() {
        return until;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void setUntil(long until) {
        this.until = until;
    }
    
    public void reset(long endTime){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    if(System.currentTimeMillis()>=endTime){
                        setBusy(false);
                        break;
                    }
                }
            }
        }).start();
    }
}

/*
 * Shaheed Ahmed Dewan Sagar
 * AUST-12-01-04-085
 * sdewan64@gmail.com
 */

package computingjobmanager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shaheed Ahmed Dewan Sagar
 *         AUST-12.01.04.085
 *         sdewan64@gmail.com
 */
class ClientInfo implements Serializable{
    
    private static final long serialVersionUID = 7863262235394607247L;
        public int id;
        public DataInputStream dataInputStream;
        public DataOutputStream dataOutputStream;
        public Socket clnt;

        public ClientInfo(int id, DataInputStream dataInputStream, DataOutputStream dataOutputStream,Socket clnt) {
            this.id = id;
            this.dataInputStream = dataInputStream;
            this.dataOutputStream = dataOutputStream;
            this.clnt = clnt;
        }
    }

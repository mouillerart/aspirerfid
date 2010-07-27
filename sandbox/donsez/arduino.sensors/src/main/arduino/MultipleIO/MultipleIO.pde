/**
 * get a command
 * repeat reads and writes and delay
 * send responses containing readen values, end of loop, end of step
 * @author Didier Donsez
 */

const int SPEED = 9600;

const byte BUFSIZE = 0xFF;
 
const byte OPMASK = 0xF0;
const byte PINMASK = 0x0F;
const int ANAVALMASK = 0x03FF;

// operations for command
const byte DIGR = 0x10;     // digital read
const byte DIGWL = 0x20;    // digital write to LOW
const byte DIGWH = 0x30;    // digital write to HIGH
const byte SETI = 0x40;     // set input mode
const byte SETO = 0x50;     // set output mode
const byte ANAW = 0x60;     // analogic write with 2 extra bytes for 10 bit value
const byte ANAR = 0x70;     // analogic read
const byte DELAY = 0x80;    // delay with 2 bytes for delay in milliseconds

// operations for response
const byte DIGRL = 0x20;    // digital read with LOW
const byte DIGRH = 0x30;    // digital read with HIGH
const byte EOST = 0x40;     // end of step with 2 bytes for the remaining loops
//const byte ANAR = 0x70;   // analogical read with 2 bytes for 10 bit value

const byte ERROR_MASK = 0xF0;      // error
const byte ERROR_BADCMD = 0xF2;    // error
const byte ERROR_BADHASH = 0xF4;    // error

// variables:
byte cmdsize;
byte cmd[BUFSIZE];
byte resmaxsize;
byte ressize;
byte res[BUFSIZE];
int loopnumber;

void setup() {
  Serial.begin(SPEED); 
}

void loop() {
 
    if (Serial.available() <= 0) {
      delay(100);
      return;
    }
    
    cmdsize = Serial.read();
    
    loopnumber = Serial.read();
    loopnumber <<=8;
    loopnumber += Serial.read();
    
    resetResponse();
    
    byte i;
    for(i=0;i<cmdsize;i++) {
      cmd[i]= Serial.read();
    }    

    //byte cmdhash= Serial.read();

    //if(!checkCmd(cmdsize,loopnumber,cmd,cmdhash)) return;

    while(loopnumber>0) {
        loopnumber--;
        for(i=0;i<cmdsize;) {
          byte op=cmd[i] & OPMASK;
          byte pin=cmd[i] & PINMASK;

          i++;

          if(op==DIGWH) {
            digitalWrite(pin,HIGH);
          } else if(op==DIGWL) { 
            digitalWrite(pin,LOW);                        
          } else if(op==SETI) {    
            pinMode(pin, INPUT);
          } else if(op==SETO) { 
            pinMode(pin, OUTPUT);
          } else if(op==DIGR) { 
            byte val=digitalRead(pin);
            res[ressize++]=((val==LOW)?DIGRL:DIGRH) | pin;               
          } else if(op==ANAR) { 
            int val= analogRead(pin);                        
            res[ressize++]=ANAR;                  
            res[ressize++]=val>>8;                    
            res[ressize++]=val & 0xFF;                    
          } else if(op==ANAW) {
            int val=(cmd[i++]<<8)+cmd[i++];
            analogWrite(pin,val & ANAVALMASK);
          } else if(op==DELAY) {
            flushResponse();
            int milli=(cmd[i++]<<8)+cmd[i++];
            delay(milli);
          } else {
            error(ERROR_BADCMD);    // error
            return;
          }
          
        }  
        // End of step
        // add EOST
        res[ressize++]=EOST;
        res[ressize++]=loopnumber>>8;                    
        res[ressize++]=loopnumber & 0xFF;                    
        flushResponse();
    }    
    // End of loop
}

/**
 * check if the cmd field are consistent 
 */
boolean checkCmd(byte cmdsize,int loopnumber, byte cmd[] ,byte hash){

   // TODO 
 // op + arg
 // pin
 // anal value
 // delay
 
   // check if size of the res will never > BUFSIZE
    int responseSize=3;  
    for(byte i=0;i<cmdsize;) {
      byte op=cmd[i] & OPMASK;
      byte pin=cmd[i] & PINMASK;
      i++;
      if(op==DIGWH) {
      } else if(op==DIGWL) { 
      } else if(op==SETI) {    
      } else if(op==SETO) { 
      } else if(op==DIGR) { 
        responseSize++;
      } else if(op==ANAR) { 
        responseSize+=3;
      } else if(op==ANAW) {
      } else if(op==DELAY) {
      } else {
        error(ERROR_BADCMD);
        return false;
      }
    }  

    if(responseSize>BUFSIZE) {
      error(ERROR_BADCMD);
      return false;
    }
  
/*  byte hash=computeHash(cmdsize,cmd);
  if(cmdhash!=hash) {
    error(ERROR_BADHASH);
    return false;  
  }    
 */
 return true;
}

/**
 * prepare and send an error
 */
void error(byte err){
      resetResponse();
      res[ressize++]=err;
      flushResponse();      
      return;
}

void flushResponse(){
  if(ressize==0) return;
  Serial.print(ressize,BYTE);
  for(byte i=0;i<ressize;i++) {
    Serial.print(res[i],BYTE);
  }
  Serial.print(computeHash(ressize,res),BYTE);
  resetResponse();   
}

void resetResponse(){
  ressize=0;   
}

/**
 * compute the hash 
 * @see http://en.wikipedia.org/wiki/List_of_checksum_algorithms
 */
byte computeHash(byte len, byte buf[]){
  return computeCRC8(len,buf);
}


/**
 * compute the Sum 8 
 */
byte computeSum8(byte len, byte buf[]){
  byte crc=len;
  while(len--){
   crc+=buf[len]; 
  }  
  return crc;
}

/**
 * compute the Longitudinal redundancy check (LRC) 
 * @see http://en.wikipedia.org/wiki/Longitudinal_redundancy_check
 */
byte computeLRC(byte len, byte buf[]){
  byte lrc=len;
  while(len--){
   lrc=(lrc | buf[len]) & ~(~lrc & buf[len]); 
  }  
}

/**
 * compute the cyclic redundancy check (CRC) 
 * @see http://en.wikipedia.org/wiki/Cyclic_redundancy_check
 */
byte computeCRC8(byte len,  byte buf[]){
   byte i ;
   byte crc = 0;
   for ( i = 0 ; i < len ; i++ ) {
      crc = addCRC8( crc, buf[i]);
   }
   crc = addCRC8(crc,0x00);
   return crc;
}

byte addCRC8(byte crc , byte ch){
   byte i ;
   for ( i = 0 ; i < 8 ; i++ ) {
      if ( crc & 0x80 ) {
         crc<<=1;
         if ( ch & 0x80 ) {
            crc = crc | 0x01;
         } else {
            crc =crc & 0xfe;
         }
         crc = crc ^ 0x85;
      } else {
        crc<<=1;
        if ( ch & 0x80 ) {
            crc = crc | 0x01;
        } else {
            crc = crc & 0xfe;
        }
      }
      ch<<=1;
   }
   return crc;
}




package edu.cedarville.cs.crypto;

public class TinyE {
	
	public static enum Mode { ECB, CBC, CTR };
	
	public Integer[] encrypt(Integer[] plaintext, Integer[] key, Mode mode, Integer[] iv) {
            
            Integer output[] = new Integer[plaintext.length];
            
            if(mode.equals(TinyE.Mode.ECB)){
                //encrypt each block individually
                for(int i=0; i<plaintext.length/2; i+=2){
                    Integer temp[] = new Integer[2];
                    temp = encryptBlock(plaintext[i], plaintext[i+1], key);
                    output[i] = temp[0];
                    output[i+1] = temp[1];
                }
                return output;
            }
            else if(mode.equals(TinyE.Mode.CBC)){
                
            }
            else if(mode.equals(TinyE.Mode.CTR)){
                
            }
            
            return null;
	}

	public Integer[] decrypt(Integer[] ciphertext, Integer[] key, Mode mode, Integer[] iv) {
            
            Integer output[] = new Integer[ciphertext.length];
            
            if(mode.equals(TinyE.Mode.ECB)){
                //encrypt each block individually
                for(int i=0; i<ciphertext.length/2; i+=2){
                    Integer temp[] = new Integer[2];
                    temp = decryptBlock(ciphertext[i], ciphertext[i+1], key);
                    output[i] = temp[0];
                    output[i+1] = temp[1];
                }
                return output;
            }
            else if(mode.equals(TinyE.Mode.CBC)){
                
            }
            else if(mode.equals(TinyE.Mode.CTR)){
                
            }
            
            return null;
	}
        
        private static Integer[] encryptBlock(Integer L, Integer R, Integer[] K){
            
            Integer delta = Integer.parseUnsignedInt("9e3779b9", 16);
            Integer sum = 0;
            
            for(int i=0; i<32; i++){
                sum += delta;
                L += ((R<<4)+K[0])^(R+sum)^((R>>5)+K[1]);
                R += ((L<<4)+K[2])^(L+sum)^((L>>5)+K[3]);
            }
            
            Integer output[] = new Integer[2];
            output[0] = L;
            output[1] = R;
            
            return output;
        }
        
        private static Integer[] decryptBlock(Integer L, Integer R, Integer[] K){
            
            Integer delta = Integer.parseUnsignedInt("9e3779b9", 16);
            Integer sum = delta << 5;
            
            for(int i=0; i<32; i++){
                R -= ((L<<4)+K[2])^(L+sum)^((L>>5)+K[3]);
                L -= ((R<<4)+K[0])^(R+sum)^((R>>5)+K[1]);
                sum -= delta;
            }
            
            Integer output[] = new Integer[2];
            output[0] = L;
            output[1] = R;
            
            return output;
        }
	
}

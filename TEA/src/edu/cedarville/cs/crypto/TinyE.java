package edu.cedarville.cs.crypto;

public class TinyE {
	
	public static enum Mode { ECB, CBC, CTR };
	
	public Integer[] encrypt(Integer[] plaintext, Integer[] key, Mode mode, Integer[] iv) {
            
            Integer output[] = new Integer[plaintext.length];
            
            //if we are using ECB mode
            if(mode.equals(TinyE.Mode.ECB)){
                
                //encrypt each block individually
                int j=0;
                for(int i=0; i<plaintext.length/2; i++){
                    Integer temp[] = new Integer[2];
                    temp = encryptBlock(plaintext[j], plaintext[j+1], key);
                    output[j] = temp[0];
                    output[j+1] = temp[1];
                    j+=2;
                }
                return output;
            }
            
            //if we are using CBC mode
            else if(mode.equals(TinyE.Mode.CBC)){
                
                //encrypt by xor-ing with the previous cipher block or iv
                int j=0;
                for(int i=0; i<plaintext.length/2; i++){    
                    Integer temp[] = new Integer[2];
                    
                    //if this is the first round, then use the iv
                    if(i==0){
                        temp = encryptBlock((plaintext[j])^(iv[0]), (plaintext[j+1])^(iv[1]), key);
                    }
                    //otherwise use the previous cipher block
                    else{
                        temp = encryptBlock((plaintext[j])^(output[j-2]), (plaintext[j+1])^(output[j-1]), key);
                    }

                    output[j] = temp[0];
                    output[j+1] = temp[1];
                    j+=2;
                }
                return output;
            }
            
            //if we are using CTR mode
            else if(mode.equals(TinyE.Mode.CTR)){
                
                //save a local copy of the iv to not mess up the regular one
                int localIV[] = new int[2];
                localIV[0] = iv[0];
                localIV[1] = iv[1];                

                //encrypt by xor-ing with the iv and an offset
                int j=0;
                for(int i=0; i<plaintext.length/2; i++){    
                    Integer temp[] = new Integer[2];
                    
                    //add i to the iv, this is a bit tricky
                    //especially since the iv was chosen to be close to overflow
                    //the iv is essentially FFFF FFFF FFFF FFF0
                    //once we get to all F's, we need to swap the left ones to 0    
                    localIV[1] = iv[1] + i;
                    if(localIV[1]>=0){
                        localIV[0] = 0;
                    }
                    
                    temp = encryptBlock(localIV[0], localIV[1], key);
                    temp[0] = temp[0] ^ plaintext[j];
                    temp[1] = temp[1] ^ plaintext[j+1];

                    output[j] = temp[0];
                    output[j+1] = temp[1];
                    j+=2;
                }
                return output;
            }
            
            return null;
	}

	public Integer[] decrypt(Integer[] ciphertext, Integer[] key, Mode mode, Integer[] iv) {
            
            Integer output[] = new Integer[ciphertext.length];
            
            //if we are using ECB mode
            if(mode.equals(TinyE.Mode.ECB)){
                
                //decrypt each block individually
                int j=0;
                for(int i=0; i<ciphertext.length/2; i++){
                    Integer temp[] = new Integer[2];
                    temp = decryptBlock(ciphertext[j], ciphertext[j+1], key);
                    output[j] = temp[0];
                    output[j+1] = temp[1];
                    j+=2;
                }
                return output;
            }
            
            //if we are using CBC mode
            else if(mode.equals(TinyE.Mode.CBC)){
                
                //decrypt by xor-ing with the previous cipher block or iv
                int j=0;
                for(int i=0; i<ciphertext.length/2; i++){    
                    Integer temp[] = new Integer[2];
                    
                    //if this is the first round, then use the iv
                    if(i==0){
                        temp = decryptBlock(ciphertext[j], ciphertext[j+1], key);
                        temp[0] = temp[0] ^ iv[0];
                        temp[1] = temp[1] ^ iv[1];
                    }
                    //otherwise use the previous cipher block
                    else{
                        temp = decryptBlock(ciphertext[j], ciphertext[j+1], key);
                        temp[0] = temp[0] ^ ciphertext[j-2];
                        temp[1] = temp[1] ^ ciphertext[j-1];
                    }

                    output[j] = temp[0];
                    output[j+1] = temp[1];
                    j+=2;
                }
                return output;
            }
            
            //if we are using CTR mode
            else if(mode.equals(TinyE.Mode.CTR)){
                
                //save a local copy of the iv to not mess up the regular one
                int localIV[] = new int[2];
                localIV[0] = iv[0];
                localIV[1] = iv[1];
                
                //decrypt by xor-ing with the iv and an offset
                int j=0;
                for(int i=0; i<ciphertext.length/2; i++){    
                    Integer temp[] = new Integer[2];
                    
                    //add i to the iv, this is a bit tricky
                    //especially since the iv was chosen to be close to overflow
                    //the iv is essentially FFFF FFFF FFFF FFF0
                    //once we get to all F's, we need to swap the left ones to 0
                    localIV[1] = iv[1] + i;
                    if(localIV[1]>=0){
                        localIV[0] = 0;
                    }
                    
                    //wow, this is an encrypt() instead of decrypt...
                    //it took me quite a while to find this bug!!
                    temp = encryptBlock(localIV[0], localIV[1], key);
                    temp[0] = temp[0] ^ ciphertext[j];
                    temp[1] = temp[1] ^ ciphertext[j+1];

                    output[j] = temp[0];
                    output[j+1] = temp[1];
                    j+=2;
                }
                return output;
            }
            
            return null;
	}
        
        //helper function that encrypts one block
        private static Integer[] encryptBlock(Integer L, Integer R, Integer[] K){
            
            //follow the very straightforward algorithm
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
        
        //helper function that decrypts one block
        private static Integer[] decryptBlock(Integer L, Integer R, Integer[] K){
            
            //follow the very straightforward algorithm
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

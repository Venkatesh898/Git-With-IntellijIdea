import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanEncoder {
    public static final int ALPHABET_sIZE=256;

        public HuffmanEncodedResult compress(final String data)
        {
            final int[] freq=buildFrequencyTable(data);
            final Node root=buildHuffmanTree(freq);
            Map<Character,String>lookupTable=buildLookupTable(root);
            return new HuffmanEncodedResult(generateEncodedData(data,lookupTable),root);


        }

    private String generateEncodedData(String data, Map<Character, String> lookupTable) {
        final StringBuilder builder =new StringBuilder();
        for(char character: data.toCharArray())
            builder.append(lookupTable.get(character));
        return builder.toString();
    }


    private static Map<Character,String> buildLookupTable(final Node root)
        {
            Map<Character,String>lookupTable=new HashMap<>();
            buildLookupTableImpl(root,"",lookupTable);
            return lookupTable;

        }

    private static void buildLookupTableImpl(Node node,
                                             String s,
                                             Map<Character, String> lookupTable) {
        if(!node.isLeaf())
        {
            buildLookupTableImpl(node.leftChild,s+'0',lookupTable);
            buildLookupTableImpl(node.rightChild,s+'1',lookupTable);
        }
        else
        lookupTable.put(node.character,s);

    }

    private static Node buildHuffmanTree(int[] freq)
        {
            final PriorityQueue<Node>pq=new PriorityQueue<>();
            for(char i=0;i<ALPHABET_sIZE;i++)
            {
                if(freq[i]>0)
                pq.add(new Node(i,freq[i],null,null ));
            }
            if(pq.size()==1)
                pq.add(new Node('\0',1,null,null));
            while(pq.size()>1)
            {
                Node left=pq.poll();
                Node right=pq.poll();
                Node parent=new Node('\0',left.frequency+right.frequency,left,right);
                pq.add(parent);
            }
            return pq.poll();

        }
        private  static int[] buildFrequencyTable(final String data)
        {
            final int[]freq=new int[ALPHABET_sIZE];
            for(char character:data.toCharArray())
                freq[character]++;
            return freq;

        }
        public String decompress(final HuffmanEncodedResult result )
        {
            final StringBuilder resultBuilder=new StringBuilder();
            Node current=result.getRoot();
            int i=0;
            while(i<result.getEncodedData().length())
            {
                while(!current.isLeaf())
                {
                    char bit=result.getEncodedData().charAt(i);
                    if(bit=='1')
                        current=current.rightChild;
                    else if (bit=='0') {
                        current=current.leftChild;

                    }
                    else
                        throw new IllegalArgumentException("Invalid bit in message"+bit);
                    i++;
                }
                resultBuilder.append(current.character);
                current=result.getRoot();

            }
            return resultBuilder.toString();

        }
        static class Node implements Comparable<Node>
        {
            private final char character;
            private int frequency;
            private Node leftChild;
            private Node rightChild;
            private Node(char character,int frequency,Node leftChild,Node rightChild)
            {
                this.character=character;
                this.frequency=frequency;
                this.leftChild=leftChild;
                this.rightChild=rightChild;
            }
            boolean isLeaf()
            {
                return this.leftChild==null&&this.rightChild==null;
            }

            @Override
            public int compareTo(Node that) {
                final int frequencyComparision=Integer.compare(this.frequency,that.frequency);
                if(frequencyComparision!=0)
                    return frequencyComparision;
                else
                    return this.character-that.character;
            }
        }
        static  class HuffmanEncodedResult
        {
            final String encodedData;
            final Node root;
            HuffmanEncodedResult(final String encodedData,final Node root)
            {
                this.encodedData=encodedData;
                this.root=root;

            }
            public Node getRoot()
            {
                return this.root;
            }
            public String getEncodedData()
            {
                return this.encodedData;
            }


        }
    public static void main(String[] args) {
        String test="abcdefg";
        //int [] ans=buildFrequencyTable(test);
        //final Node n=buildHuffmanTree(ans);
        //Map<Character,String>mp=buildLookupTable(n);
        HuffmanEncoder encoder=new HuffmanEncoder();
        HuffmanEncodedResult result=encoder.compress(test);
        System.out.println("encoded message:"+result.encodedData);
        System.out.println("decoded message:"+encoder.decompress(result));


    }



}

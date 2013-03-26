/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 3/26/13
 * Time: 10:08 PM
 */
public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns)
    {
        int minDis = Integer.MAX_VALUE;
        String minString = null;
        for (String s : nouns)
        {
            int dis = 0;
            for (String d : nouns)
            {
                int _dis = wordNet.distance(s, d);
                if (_dis != -1)
                {
                    dis += _dis;
                }
                else
                {
                    dis += wordNet.wordCount();
                }
            }
            if (dis < minDis)
            {
                minDis = dis;
                minString = s;
            }
        }
        return minString;
    }
}

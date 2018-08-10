import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PokerGame 
{
	ArrayList<Card> cards;
	int size;
	WinTypes max=WinTypes.HighCard;
	ArrayList<int[]> count=new ArrayList<int[]>();
	PokerGame(int size)
	{
		this.size=size;
		for(int i=0;i<size;i++)
		{
			Card c=new Card().getRandom();
			int j=0;
			for(;j<i;j++)
				if(cards.get(i).getValue()>=cards.get(j).getValue())
					break;
					
			cards.add(j,c);
		}
	}
	
	public void getBestFive()
	{
		BestFive set=new BestFive();
		getFiveCards(set,0,0);
	}
	
	void getFiveCards(BestFive set,int idx,int filled)
	{
		if(filled==set.cards.length)
		{
			for(int i=0;i<set.cards.length;i++)
			{
				int j=0;
				for(;j<set.count.size();j++)
					if(set.count.get(j)[0]==set.cards[i].value)
						set.count.get(j)[1]++;
				if(j==set.count.size())
				{
					set.count.add(new int[]{set.cards[i].value,1});
				}
				set.suiteCount[set.cards[i].suite.ordinal()]++;
			}
			checkBestFive(set);
			return;
		}
		
		if(idx>=size)
			return;
		set.cards[filled]=cards.get(idx);
		getFiveCards(set,idx+1,filled);
		getFiveCards(set,idx+1,filled+1);
	}
	
	public boolean isGreater(ArrayList<int[]> count1,ArrayList<int[]> count2)
	{
		for(int i=0;i<count1.size();i++)
			if(count1.get(0)[0]>count2.get(0)[0])
				return true;
		return false;
	}
	
	public void setMax(WinTypes type, BestFive set) 
	{
		if(max.ordinal()>type.ordinal())
			return;
		if(isGreater(set.count,count))
			count=set.count;
		max=type;
	}
	
	public void checkBestFive(BestFive set)
	{
		Collections.sort(set.count, new Comparator<int[]>() 
		{
			public int compare(int[] r1, int[] r2) 
			{
				if(r1[1]<r2[1]) 
					return 1;
				else if(r1[1]==r2[1] && r1[0]<r2[0])
					return 1;
				return -1;
			}
		});
		
		if(set.isRoyalFlush())
		{
			max=WinTypes.RoyalFlush;
		}
		else if(set.isStraightFlush())
		{
			setMax(WinTypes.StraightFlush,set);
		}
		else if(set.isFourOfAKind())
		{
			setMax(WinTypes.FourOfAKind,set);
			max=WinTypes.FourOfAKind;
		}
		else if(set.isFullHouse())
		{
			setMax(WinTypes.FullHouse,set);
		}
		else if(set.isFlush())
		{
			setMax(WinTypes.Flush,set);
		}
		else if(set.isStraight())
		{
			setMax(WinTypes.Straight,set);
		}
		else if(set.isThreeOfaKind())
		{
			setMax(WinTypes.ThreeOfaKind,set);
		}
		else if(set.isTwoPair())
		{
			setMax(WinTypes.TwoPair,set);
		}
		else if(set.isOnePair())
		{
			setMax(WinTypes.OnePair,set);
		}
		else
		{
			setMax(WinTypes.HighCard,set);
		}
	}
}

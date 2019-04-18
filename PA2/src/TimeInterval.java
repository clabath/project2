
public class TimeInterval {
	private int start, end;
	public TimeInterval(int x, int y)
	{
		start = x;
		end = y;
	}
	public int getStart()
	{
		return start;
	}
	public int getEnd()
	{
		return end;
	}
	
	public boolean isWithin(ComputerNode N)
	{
		if(N.getTimestamp() > start && N.getTimestamp() < end)
		{
			return true;
		}
		
		return false; 
	}
}

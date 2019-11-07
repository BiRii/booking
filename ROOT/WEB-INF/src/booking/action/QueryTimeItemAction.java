package booking.action;

import java.util.*;
import com.opensymphony.xwork2.ActionContext;

import booking.action.base.BaseAction;
import booking.po.*;

public class QueryTimeItemAction extends BaseAction
{
	private String str;
	private String username;
	private List<Timeitem> timeItemList = new ArrayList<Timeitem>();
	private int firstPagination;
	private int lastPagination;
	private int pagination = 1;
	private int pages;
	private List<Timeitem> subtimeItemList = new ArrayList<Timeitem>();	
	
	//str属性的setter和getter方法
	public void setStr(String str)
	{
		this.str = str;
	}
	public String getStr()
	{
		return this.str;
	}

	//username属性的setter和getter方法
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getUsername()
	{
		return this.username;
	}

	//timeItemList属性的setter和getter方法
	public void setTimeItemList(List<Timeitem> timeItemList)
	{
		this.timeItemList = timeItemList;
	}
	public List<Timeitem> getTimeItemList()
	{
		 return this.timeItemList;
	}

	//firstPagination属性的setter和getter方法
	public void setFirstPagination(int firstPagination)
	{
		this.firstPagination = firstPagination;
	}
	public int getFirstPagination()
	{
		return this.firstPagination;
	}

	//lastPagination属性的setter和getter方法
	public void setLastPagination(int lastPagination)
	{
		this.lastPagination = lastPagination;
	}
	public int getLastPagination()
	{
		return this.lastPagination;
	}

	//pagination属性的setter和getter方法
	public void setPagination(int pagination)
	{
		this.pagination = pagination;
	}
	public int getPagination()
	{
		return this.pagination;
	}

	//pages属性的setter和getter方法
	public void setPages(int pages)
	{
		this.pages = pages;
	}
	public int getPages()
	{
		return this.pages;
	}

	//subtimeItemList属性的setter和getter方法
	public void setSubtimeItemList(List<Timeitem> subtimeItemList)
	{
		this.subtimeItemList = subtimeItemList;
	}
	public List<Timeitem> getSubtimeItemList()
	{
		 return this.subtimeItemList;
	}

	public String execute() throws Exception
	{
		String sn = (String)ActionContext.getContext().getSession().get("sessusername");
		if(sn==null)
		{
			setStr("s1");	//会话已失效，请重新登录
			return ERROR;
		}

		if(!username.equals(sn))
		{
			setStr("s2");	//帐户username已退出，现在的帐户是sn，点击确定或关闭后将返回sn的管理界面
			return ERROR;
		}
		
		List<Integer> prerogative = bookbns.getPrerogativeByName(sn);

		if(prerogative.isEmpty())
		{
			setStr("s3");	//系统已不存在当前登录用户
			ActionContext.getContext().getSession().put("sessusername", null);
			return ERROR;
		}

		if(prerogative.get(0) != 1)
		{
			setStr("s4");	//当前登录用户不是管理员，将返回首页
			return ERROR;
		}

		timeItemList = bookbns.getAllTimeItem();
		
		if(timeItemList.isEmpty())
		{
			return SUCCESS;			
		}

		int pgsize = timeItemList.size();
		
		if(pgsize%15 != 0)
		{
			pages = pgsize/15 + 1;
		}
		else
		{
			pages = pgsize/15;
		}

		if(pagination > pages)
		{
			pagination = pages;
		}
		
		int fromIndex = (pagination-1)*15;
		int reachableIndex = fromIndex + 15;
		int toIndex;
		
		if(pgsize > reachableIndex)
		{
			toIndex = reachableIndex;
		}
		else
		{
			toIndex = pgsize;
		}

		subtimeItemList = timeItemList.subList(fromIndex, toIndex);

		int reachablePagination;
		if(pagination == 1)
		{
			reachablePagination = 10;
		}
		else
		{
			reachablePagination = pagination + 8;
		}
		
		if(pages > reachablePagination)
		{
			lastPagination = reachablePagination;
		}
		else
		{
			lastPagination = pages;
		}
		
		if(lastPagination > 9)
		{
			firstPagination = lastPagination - 9;
		}
		else
		{
			firstPagination = 1;
		}

		return SUCCESS;
	}
}
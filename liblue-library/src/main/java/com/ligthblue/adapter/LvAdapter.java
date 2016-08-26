package com.ligthblue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
/**
 * ListView万能适配器
 * Created by 何成龙 on 2015/7/6.
 */
public abstract class LvAdapter<T> extends BaseAdapter
{
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;

	public LvAdapter(Context context, List<T> mDatas, int itemLayoutId)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final LvViewHolder lvViewHolder = getViewHolder(position, convertView,
				parent);
		convert(lvViewHolder, getItem(position),position);
		lvViewHolder.setmPosition(position);
		
		return lvViewHolder.getConvertView();

	}

	public abstract void convert(LvViewHolder helper, T item, int position);

	private LvViewHolder getViewHolder(int position, View convertView,
									   ViewGroup parent)
	{
		return LvViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}
	
	

}

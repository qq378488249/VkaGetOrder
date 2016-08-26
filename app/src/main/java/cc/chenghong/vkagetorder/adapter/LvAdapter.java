package cc.chenghong.vkagetorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collection;
import java.util.List;

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

	/**
	 * 附加新的列表
	 *
	 * @param list
	 */
	public void appendList(List<T> list) {
		// TODO Auto-generated method stub
		this.mDatas.addAll((Collection<? extends T>) list);
		notifyDataSetChanged();
	}

	/**
	 * 清除旧列表，显示新的列表
	 *
	 * @param list
	 */
	public void showNewList(List<T> list) {
		// TODO Auto-generated method stub
		this.mDatas.clear();
		this.mDatas.addAll(list);
		notifyDataSetChanged();
	}
	/**
	 * 添加列表
	 *
	 * @param list
	 */
	public void addAll(List<T> list) {
		this.mDatas.addAll((Collection<? extends T>) list);
		notifyDataSetChanged();
	}

	/**
	 * 在末尾插入数据
	 *
	 * @param t
	 */
	public void add(T t) {
		this.mDatas.add(t);
		notifyDataSetChanged();
	}

	/**
	 * 在指定位置插入数据
	 *
	 * @param t
	 * @param position
	 */
	public void add(int position,T t) {
		this.mDatas.add(position, t);
		notifyDataSetChanged();
	}

	public void remove( int location){
		this.mDatas.remove(location);
		notifyDataSetChanged();
	}

	public void clear(){
		this.mDatas.clear();
		notifyDataSetChanged();
	}

}

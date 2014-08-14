##框架开发文档

###一、Http请求Bitmap本地缓存/内存缓存<br />
采用Lru缓存策略，可以轻松实现对本地/内存缓存管理，灵活控制缓存大小。<br />
使用说明：<br />

<code>
BitmapManager bitmapManager = new BitmapManager(mContext,BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_launcher));
bitmapManager.loadBitmap(imageurl,imageview);
</code>

直接给定图片网络地址，自动将图片缓存到本地/内存，如果图片已经存在将直接取本地/内存  <br />

###二、Http请求xml/json数据  <br />
同样采用LRU缓存策略，灵活控制缓存大小、缓存有效期、清理缓存等  <br />
使用说明：<br />
你将需要一个类来继承HttpConnectPools(网络连接池)，然后去实现里面的方法：<br />
getMethod(请求方法DoGet/DoPost)  <br />
getHttpUrl(请求url)  <br />
getRequestParams(获取请求参数)  <br />
parseReponseData(解析响应数据)  <br />
如果是DoPost请求还需要实现：  <br />
getPostTextContentMap(Post文本内容)  <br />
getPostAudioPathMap(Post音频)  <br />
getPostImagePathMap(Post图片)  <br />
当然你还需要一个方法将外面的参数给传进来：  <br />
setRequestParams(设置请求参数)  <br />
当一切准备就绪，我们就可以发起一个网络请求： <br />

<pre><code >

Handler mHandler = new HttpHandler(this) {
@Override
protected void httpSucced(Serializable serObject) {
	// TODO Auto-generated method stub
	super.httpSucced(serObject);
	rssList = (List<TestItem>)serObject;
	if(rssList != null && rssList.size() > 0){
		newsAdapter.setRSSList(rssList);
		newsAdapter.notifyDataSetChanged();
	}
}

@Override
protected void httpFailed(Exception exception) {
	// TODO Auto-generated method stub
	super.httpFailed(exception);
	AppLog.e(tag, exception.getMessage());
}
};
TestItemRequest rssaaRequest = new TestItemRequest(this, mHandler);
rssaaRequest.setCacheEnable(true);
rssaaRequest.create();
			
</code></pre>

请求成功后，自动将数据缓存本地/内存，下次请求将直接读取内存/本地，当然你也可以setCacheEnable(false);控制不取缓存，每个请求对应一个Handler，他将为我们处理更新UI  <br />

###三、SQLite数据库存储  <br />
SQLite封装将为我们很轻松的建表建库，尽量节省一些臃肿的代码。  <br />
使用说明： <br />
所有的数据库表统一要继承BaseTableColumns类，在这里帮我们很轻松构建表结构直接生成数据表  <br />
所有的数据库操作统一要继承BaseDao，在这里为我们封装了所有DAO方法  <br />

<pre><code >
UserDao userDao = new UserDao(this);
//ContentValues cvalues = new ContentValues();
//cvalues.put(UserColumns.USER_URL, "0987654321");
//cvalues.put(UserColumns.USER_CONTENT, "0987654321");
//cvalues.put(UserColumns.USER_TIME, "07654321");
//boolean isuccess = userDao.insert(cvalues);   
//boolean isuccess = userDao.update(cvalues, UserColumns._ID + "=?", new String[]{"1"});
//boolean isuccess = userDao.delete(UserColumns._ID + "=?", new String[]{"1"});
</code></pre>

以上的简单的测试案例仅供参考。<br />

###四、ContentProvider数据共享  <br />

<pre><code >
ContentValues values = new ContentValues();
//values.put("url", "3478");
//values.put("content", "56789");
//values.put("update_time", "123456789");
//getContentResolver().insert(Uri.parse("content://com.gyz.myandroidframe.dao/plugins"),values);
//getContentResolver().update(Uri.parse("content://com.gyz.myandroidframe.dao/plugins"), values, BaseColumns._ID + "=?", new String[]{"3"});
//getContentResolver().delete(Uri.parse("content://com.gyz.myandroidframe.dao/plugins"), BaseColumns._ID + "=?", new String[]{"1"});
//Cursor mCursor = getContentResolver().query(Uri.parse("content://com.gyz.myandroidframe.dao/plugins"), null, null, null, null);

</code></pre>

以上是简单的测试案例仅供参考。<br />


###五、一些工具类<br /><br /><br />















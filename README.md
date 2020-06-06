# ScreenSnap  

### 功能  
- 屏幕取色
- 屏幕二维码扫描
- 屏幕截图

### 问题解决  

1. __截图界面错位或模糊：__  
添加VM配置 `-Dsun.java2d.uiScale=1`

2. __Windows下中文乱码：__  
添加VM配置 `-Dfile.encoding=GBK`

3. __resources加载出错：__  
在Artifacts里把resources添加到Directory Content

4. __程序卡住：__  
选中Windows自带控制台的内容时，控制台会在输出命令处暂停程序，结果导致程序卡住。右键控制台，让程序继续运行
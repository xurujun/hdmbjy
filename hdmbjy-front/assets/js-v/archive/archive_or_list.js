var vm = new Vue({
	el: '#app',
	
	data:{
		varList: [],	//list
		page: [],		//分页类
		username: '',		//当前用户
		adminname: '',		//管理员用户
		KEYWORDS:'',	//检索条件,关键词
		showCount: -1,	//每页显示条数(这个是系统设置里面配置的，初始为-1即可，固定此写法)
		currentPage: 1,	//当前页码
		nginxurl : '',
		DEPARTMENT_ID : '',
		add:false,		//增
		del:false,		//删
		edit:false,		//改
		toExcel:false,	//导出到excel权限
		loading:false,	//加载状态
		RECORD_TYPES:[] ,   //档案类型集合
		TYPE:'',   //档案类型
		isAdmin:false,  //是否是管理员
    },
    
	methods: {
		
        //初始执行
        init() {
        	var FID = this.getUrlKey('oId');
        	this.DEPARTMENT_ID = FID;
        	//复选框控制全选,全不选 
    		$('#zcheckbox').click(function(){
	    		 if($(this).is(':checked')){
	    			 $("input[name='ids']").click();
	    		 }else{
	    			 $("input[name='ids']").attr("checked", false);
	    		 }
    		});
    		this.getList();
    		this.nginxurl = nginxurl;
        },
        //预览
        preview:function(url){
        	var diag = new top.Dialog();
			diag.Drag=true;
			diag.Title ="预览";
			diag.URL = '../../archive/archive/archive_preview.html?pdf='+url;
			diag.Width = 950;
			diag.Height = 850;
			diag.Modal = false;			//有无遮罩窗口
			diag.CancelEvent = function(){ //关闭事件
			    diag.close();
			};
			diag.show();
        },
        //下载
        download:function(url,name){
        	
        	
        	$.ajax({
        		xhrFields: {
                    withCredentials: true
                },
        		type: "POST",
        		url: httpurl+'archive/isFile',
        		data: {NAME:name,
        			URL:url,
        			tm:new Date().getTime()},
        		dataType:"json",
        		success: function(data){
        		 if("success" == data.result){
        			 swal("","文件不存在", "warning");
        		 }else if ("update" == data.result){
        			 window.location.href=httpurl+"archive/download?URL="+url+"&NAME="+name;
                 }else if ("exception" == data.result){
                 	showException("文件系统",data.exception);//显示异常
                 }
        		}
        	}).done().fail(function(){
                swal("登录失效!", "请求服务器无响应，稍后再试", "warning");
                setTimeout(function () {
                	window.location.href = "../../login.html";
                }, 2000);
            });
        },
        //批量下载
        downloadAll:function(msg){
        	//window.location.href=httpurl+"archive/download?URL="+url+"&NAME="+name;
        	swal({
                title: '',
                text: msg,
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDelete) => {
                if (willDelete) {
    	        	var str = '';
    				for(var i=0;i < document.getElementsByName('ids').length;i++){
    					  if(document.getElementsByName('ids')[i].checked){
    					  	if(str=='') str += document.getElementsByName('ids')[i].value;
    					  	else str += ',' + document.getElementsByName('ids')[i].value;
    					  }
    				}
    				if(str==''){
    					$("#cts").tips({
    						side:2,
    			            msg:'点这里全选',
    			            bg:'#AE81FF',
    			            time:3
    			        });
    	                swal("", "您没有选择任何内容!", "warning");
    					return;
    				}else{
    					if(msg == '确定要下载选中的附件吗?'){
    						//this.loading = true;
    						$.ajax({
    			        		xhrFields: {
    			                    withCredentials: true
    			                },
    			        		type: "POST",
    			        		url: httpurl+'archive/isFileAll',
    			        		data: {DATA_IDS:str,
    			        			tm:new Date().getTime()},
    			        		dataType:"json",
    			        		success: function(data){
    			        		 if("success" == data.result){
    			        			 if(data.pdss.length>0){
    			        				 var title = "";
    			        				 for (var i = 0; i < data.pdss.length; i++) {
    			        					 title += data.pdss[i].title+"、"
										 }
    			        			 }
    			        			 swal("", title+"文件不存在", "warning");
    			        			 //swal(title+"文件不存在");
    			        			 if(data.count != data.countZ){
    			        				 window.location.href= httpurl+'archive/downloadAll?tm='+new Date().getTime()+"&DATA_IDS="+str;
    			        			 }
    			        		 }else if ("exception" == data.result){
    			                 	showException("文件系统",data.exception);//显示异常
    			                 }
    			        		}
    			        	}).done().fail(function(){
    			                swal("登录失效!", "请求服务器无响应，稍后再试", "warning");
    			                setTimeout(function () {
    			                	window.location.href = "../../login.html";
    			                }, 2000);
    			            });
    			        	
    						
    						
    						
    						
    						
    						/*$.ajax({
    							xhrFields: {
    	                            withCredentials: true
    	                        },
    							type: "POST",
    							url: httpurl+'archive/downloadAll?tm='+new Date().getTime(),
    					    	data: {DATA_IDS:str},
    							dataType:'json',
    							success: function(data){
    								if("success" == data.result){
    									swal("下载成功", "请查看!", "success");
    		        				 }
    								vm.getList();
    							}
    						});*/
    					}
    				}
                }
            });
        	
        
        },
        //获取列表
        getList: function(){
        	this.loading = true;
        	$.ajax({
        		xhrFields: {
                    withCredentials: true
                },
        		type: "POST",
        		url: httpurl+'archive/list?showCount='+this.showCount+'&currentPage='+this.currentPage,
        		data: {KEYWORDS:this.KEYWORDS,
        			DEPARTMENT_ID:this.DEPARTMENT_ID,
					TYPE:this.TYPE,
					FIND_TYPE:2,
        			tm:new Date().getTime()
					},
        		dataType:"json",
        		success: function(data){
        		 if("success" == data.result){
        			 vm.adminname = data.adminname;
        			 vm.varList = data.varList;
        			 vm.page = data.page;
        			 vm.username = data.username;
        			 vm.hasButton();
					 vm.getDict();
					 vm.isAdmin=data.isAdmin;
        			 vm.loading = false;
        			 $("input[name='ids']").attr("checked", false);
        		 }else if ("exception" == data.result){
                 	showException("文件系统",data.exception);//显示异常
                 }
        		}
        	}).done().fail(function(){
                swal("登录失效!", "请求服务器无响应，稍后再试", "warning");
                setTimeout(function () {
                	window.location.href = "../../login.html";
                }, 2000);
            });
        },
        
    	//新增
    	goAdd: function (){
    		 var diag = new top.Dialog();
    		 diag.Drag=true;
    		 diag.Title ="新增";
    		 diag.URL = '../../archive/archive/archive_edit.html?oId='+vm.DEPARTMENT_ID;
    		 diag.Width = 1000;
    		 diag.Height = 800;
    		 diag.Modal = true;				//有无遮罩窗口
    		 diag. ShowMaxButton = true;	//最大化按钮
    	     diag.ShowMinButton = true;		//最小化按钮 
    		 diag.CancelEvent = function(){ //关闭事件
    	    	 var varSon = diag.innerFrame.contentWindow.document.getElementById('showform');
    			 if(varSon != null && varSon.style.display == 'none'){
    				 vm.getList();
    			}
    			diag.close();
    		 };
    		 diag.show();
    	},
    	
    	//修改
    	goEdit: function(id){
    		 var diag = new top.Dialog();
    		 diag.Drag=true;
    		 diag.Title ="编辑";
    		 diag.URL = '../../archive/archive/archive_edit.html?FID='+id;
    		 diag.Width = 1000;
    		 diag.Height = 800;
    		 diag.Modal = true;				//有无遮罩窗口
    		 diag. ShowMaxButton = true;	//最大化按钮
    	     diag.ShowMinButton = true;		//最小化按钮 
    		 diag.CancelEvent = function(){ //关闭事件
    			 var varSon = diag.innerFrame.contentWindow.document.getElementById('showform');
    			 if(varSon != null && varSon.style.display == 'none'){
    				 vm.getList();
    			}
    			diag.close();
    		 };
    		 diag.show();
    	},
    	
    	//删除
    	goDel: function (id){
    		swal({
    			title: '',
                text: "确定要删除吗?",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDelete) => {
                if (willDelete) {
                	this.loading = true;
                	$.ajax({
                		xhrFields: {
                            withCredentials: true
                        },
            			type: "POST",
            			url: httpurl+'archive/delete',
            	    	data: {ARCHIVE_ID:id,tm:new Date().getTime()},
            			dataType:'json',
            			success: function(data){
            				 if("success" == data.result){
            					 swal("删除成功", "已经从列表中删除!", "success");
            				 }
            				 vm.getList();
            			}
            		});
                }
            });
    	},
    	getDict: function (){
    		$.ajax({
    			xhrFields: {
    	            withCredentials: true
    	        },
    			type: "POST",
    			url: httpurl+'dictionaries/getLevelsByNameEn',
    			data: {NAME_EN:'RECORD_TYPES',tm:new Date().getTime()},//类型
    			dataType:'json',
    			success: function(data){
    				vm.RECORD_TYPES=data.list;
					
					$("#TYPE").html('<option value="">请选择档案类型</option>');
					 $.each(data.list, function(i, dvar){
						 $("#TYPE").append("<option value="+dvar.BIANMA+">"+dvar.NAME+"</option>");
					 });
    			},
    			error:function(){
    			alert("错误");	
    			}
    		});
    	},
		
    	//批量操作
    	makeAll: function (msg){
    		swal({
                title: '',
                text: msg,
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDelete) => {
                if (willDelete) {
    	        	var str = '';
    				for(var i=0;i < document.getElementsByName('ids').length;i++){
    					  if(document.getElementsByName('ids')[i].checked){
    					  	if(str=='') str += document.getElementsByName('ids')[i].value;
    					  	else str += ',' + document.getElementsByName('ids')[i].value;
    					  }
    				}
    				if(str==''){
    					$("#cts").tips({
    						side:2,
    			            msg:'点这里全选',
    			            bg:'#AE81FF',
    			            time:3
    			        });
    	                swal("", "您没有选择任何内容!", "warning");
    					return;
    				}else{
    					if(msg == '确定要删除选中的数据吗?'){
    						this.loading = true;
    						$.ajax({
    							xhrFields: {
    	                            withCredentials: true
    	                        },
    							type: "POST",
    							url: httpurl+'archive/deleteAll?tm='+new Date().getTime(),
    					    	data: {DATA_IDS:str},
    							dataType:'json',
    							success: function(data){
    								if("success" == data.result){
    									swal("删除成功", "已经从列表中删除!", "success");
    		        				 }
    								vm.getList();
    							}
    						});
    					}
    				}
                }
            });
    	},
        
      	//判断按钮权限，用于是否显示按钮
        hasButton: function(){
        	var keys = 'archive:add,archive:del,archive:edit,toExcel';
        	$.ajax({
        		xhrFields: {
                    withCredentials: true
                },
        		type: "POST",
        		url: httpurl+'head/hasButton',
        		data: {keys:keys,tm:new Date().getTime()},
        		dataType:"json",
        		success: function(data){
        		 if("success" == data.result){
        			vm.add = data.archivefhadminadd;		//新增权限
        			vm.del = data.archivefhadmindel;		//删除权限
        			vm.edit = data.archivefhadminedit;	//修改权限
        			vm.toExcel = data.toExcel;						//导出到excel权限
        		 }else if ("exception" == data.result){
                 	showException("按钮权限",data.exception);		//显示异常
                 }
        		}
        	})
        },
        
        //导出excel
		goExcel: function (){
			swal({
               	title: '',
                text: '确定要导出到excel吗?',
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDelete) => {
                if (willDelete) {
                	window.location.href = httpurl+'archive/excel';            	
                }
            });
		},
        
        //-----分页必用----start
        nextPage: function (page){
        	this.currentPage = page;
        	this.getList();
        },
        changeCount: function (value){
        	this.showCount = value;
        	this.getList();
        },
        toTZ: function (){
        	var toPaggeVlue = document.getElementById("toGoPage").value;
        	if(toPaggeVlue == ''){document.getElementById("toGoPage").value=1;return;}
        	if(isNaN(Number(toPaggeVlue))){document.getElementById("toGoPage").value=1;return;}
        	this.nextPage(toPaggeVlue);
        }
       //-----分页必用----end
        ,
    	//根据url参数名称获取参数值
        getUrlKey: function (name) {
            return decodeURIComponent(
                (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null;
        }
	},
	watch:{
		 TYPE: function (newValue, oldValue) {
		    if(newValue!=oldValue){
				vm.getList();
			}
		}
	},
	
	mounted(){
        this.init();
    }
})

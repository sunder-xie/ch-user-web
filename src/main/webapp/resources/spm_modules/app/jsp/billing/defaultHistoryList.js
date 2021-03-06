define('app/jsp/billing/defaultHistoryList', function (require, exports, module) {
    'use strict';
    var $=require('jquery'),
    Widget = require('arale-widget/1.2.0/widget'),
    Dialog = require("optDialog/src/dialog"),
    Paging = require('paging/0.0.1/paging-debug'),
    Uploader = require('arale-upload/1.2.0/index'),
    AjaxController = require('opt-ajax/1.0.0/index'),
    Calendar = require('arale-calendar/1.1.2/index');
    
    require("jsviews/jsrender.min");
    require("jsviews/jsviews.min");
    require("bootstrap-paginator/bootstrap-paginator.min");
    require("app/util/jsviews-ext");
    
    require("opt-paging/aiopt.pagination");
    require("twbs-pagination/jquery.twbsPagination.min");
    
    //实例化AJAX控制处理对象
    var ajaxController = new AjaxController();
    //定义页面组件类
    var DefaultHistoryPager = Widget.extend({
    	
    	//属性，使用时由类的构造函数传入
    	attrs: {
    	},
    	Statics: {
    		DEFAULT_PAGE_SIZE: 5
    	},
    	//事件代理
    	events: {
        },
    	//重写父类
    	setup: function () {
    		DefaultHistoryPager.superclass.setup.call(this);
    		this._queryDefaultHistoryList();
    	},
    	
    	//获取违约历史列表
    	_queryDefaultHistoryList: function(){
    		var _this = this;
    		$("#pagination-ul").runnerPagination({
    			url: _base+"/defaultManager/getDefaultHistoryList",
	 			method: "POST",
	 			dataType: "json",
	 			processing: true,
	 			messageId:"showMessageDiv",
	 			renderId:"TBODY_DEFAULTLIST",
	            data : {
					"tenantId": 'ch',
					"userId":userId,
					"loginName":loginNameInfo,
					"custName":custNameInfo,
				},
	           	pageSize: DefaultHistoryPager.DEFAULT_PAGE_SIZE,
	           	visiblePages:5,
	            message: "正在为您查询数据..",
	            callback: function(data){
	              	if(data.result != null && data.result != 'undefined' && data.result.length>0){
	            		var template = $.templates("#defaultListImpl");
	                    var htmlOutput = template.render(data);
	                    $("#TBODY_DEFAULTLIST").html(htmlOutput);
	            	}
	            }
    		}); 
    	},

    	
    });
    
    module.exports = DefaultHistoryPager
});
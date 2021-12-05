(function($) {
	let files = {
		uploadClass: 'https://qcloud-1256166828.cos.ap-shanghai.myqcloud.com/script/javascript/uploader/web-uploader/0.1.5/web-uploader.css',
		uploadScript: 'https://qcloud-1256166828.cos.ap-shanghai.myqcloud.com/script/javascript/uploader/web-uploader/0.1.5/web-uploader.js',
		swf: 'https://qcloud-1256166828.cos.ap-shanghai.myqcloud.com/script/javascript/uploader/web-uploader/0.1.5/uploader.swf',
		server: 'uploader/file'
	}

	$.extend({
		/**
		 * 上传控件初始化方法参数说明:
		 * $.uploaderInit({
		 * swf {String} [可选] swf文件路径
		 * server {String} [可选] [默认值:'uploader/file'] 文件接收服务端路径
		 * dnd {Selector} [可选] [默认值:undefined] 指定拖拽的容器,如果不指定,则不启动
		 * disableGlobalDnd {Selector} [可选] [默认值:false] 是否禁掉整个页面的拖拽功能,如果不禁用,图片拖进来的时候会默认被浏览器打开
		 * paste {Selector} [可选] [默认值:document.body] 指定监听粘贴事件的容器,如果不指定,不启用此功能。此功能为通过粘贴来添加截屏的图片
		 * pick {Selector, Object} [可选] 指定选择文件的按钮容器,不指定则不创建按钮。
		 *      .id {Seletor|dom} 指定选择文件的按钮容器,不指定则不创建按钮。
		 *      注意 这里虽然写的是 id, 但 不是只支持 id, 还支持 class, 或者 dom 节点。
		 *      .innerHTML {String} 指定按钮文字。不指定时优先从指定的容器中看是否自带文字。
		 *      .multiple {Boolean} [默认值:false] 是否开起同时选择多个文件的能力。
		 * accept {Arroy} [可选] 指定接受哪些类型的文件。 由于目前还有ext转mimeType表,所以这里需要分开指定。
		 *      .title {String} 文字描述
		 *      .extensions {String} 允许的文件后缀,不带点,多个用逗号分割,示例:gif,jpg,jpeg,png,mp4,mp3
		 *      .mimeTypes {String} 多个用逗号分割。示例:image/*,video/*
		 * thumb {Object} [可选] 配置生成缩略图的选项。
		 *      .width {int} [可选] [默认值:300] 缩略图宽.
		 *      .height {int} [可选] [默认值:300] 缩略图高.
		 *      .quality {int} [可选] [默认值:70] 图片质量,只有type为`image/jpeg`的时候才有效。
		 *      .allowMagnify {Boolean} [可选] [默认值:false] 是否允许放大,如果想要生成小图的时候不失真,此选项应该设置为false
		 *      .crop {Boolean} [可选] [默认值:true] 是否允许裁剪,裁剪掉图片四周多余的部分
		 *      .type {String} [可选] [默认值:undefined] 为空的话则保留原有图片格式,否则强制转换成指定的类型
		 * compress {Object} [可选] 配置压缩的图片的选项。如果此选项为false,则图片在上传前不进行压缩
		 *      .width {int} [可选] [默认值:1600] 尝试压缩的图片宽度
		 *      .height {int} [可选] [默认值:1600] 尝试压缩的图片高度
		 *      .quality {int} [可选] [默认值:90] 图片质量,只有type为`image/jpeg`的时候才有效
		 *      .allowMagnify {Boolean} [可选] [默认值:false] 是否允许放大,如果想要生成小图的时候不失真,此选项应该设置为false
		 *      .crop {Boolean} [可选] [默认值:false] 是否允许裁剪,裁剪掉图片四周多余的部分
		 *      .preserveHeaders {Boolean} [可选] [默认值:true] 是否保留头部meta信息
		 *      .noCompressIfLarger {Boolean} [可选] [默认值:false] 如果发现压缩后文件大小比原来还大,则使用原来图片
		 *      .compressSize  {int} [可选] [默认值:1048576] 单位:字节,如果图片大小小于此值,不会采用压缩,默认1M
		 * auto {Boolean} [可选] [默认值：false] 设置为 true 后,不需要手动调用上传,有文件选择即开始上传
		 * runtimeOrder {Object} [可选] [默认值:html5,flash] 指定运行时启动顺序。默认会想尝试 html5 是否支持,如果支持则使用 html5,否则则使用 flash
		 * 可以将此值设置成 flash,来强制使用 flash 运行
		 * prepareNextFile {Boolean} [可选] [默认值:true] 是否允许在文件传输时提前把下一个文件准备好。
		 * 对于一个文件的准备工作比较耗时,比如图片压缩，md5序列化。如果能提前在当前文件传输期处理,可以节省总体耗时。
		 * chunked {Boolean} [可选] [默认值:false] 是否要分片处理大文件上传
		 * chunkSize {Boolean} [可选] [默认值:5242880] 如果要分片,分多大一片?默认大小为5M
		 * chunkRetry {Boolean} [可选] [默认值:2] 如果某个分片由于网络问题出错,允许自动重传多少次?
		 * threads {Boolean} [可选] [默认值:3] 上传并发数,允许同时最大上传进程数(是进程数不是文件数,如果开启分片就是同时上传的分片数)
		 * formData {Object} [可选] [默认值:{}] 文件上传请求的参数表,每次发送都会发送此对象中的参数
		 * fileVal {Object} [可选] [默认值:'file'] 设置文件上传域的name
		 * method {Object} [可选] [默认值:'POST'] 文件上传方式,POST或者GET。
		 * sendAsBinary {Object} [可选] [默认值:false] 是否已二进制的流的方式发送文件,这样整个上传内容php://input都为文件内容,其他参数在$_GET数组中
		 * fileNumLimit {int} [可选] [默认值:undefined] 验证文件总数量, 超出则不允许加入队列。
		 * fileSizeLimit {int} [可选] [默认值:undefined] 验证文件总大小是否超出限制, 超出则不允许加入队列。
		 * fileSingleSizeLimit {int} [可选] [默认值:undefined] 验证单个文件大小是否超出限制, 超出则不允许加入队列。
		 * duplicate {Boolean} [可选] [默认值:false] 是否允许重复的文件 去重,根据文件名字、文件大小和最后修改时间来生成hash Key.
		 * disableWidgets {String, Array} [可选] [默认值:undefined] 默认所有 Uploader.register 了的 widget 都会被加载
		 * 如果禁用某一部分，请通过此 option 指定黑名单
		 * })
		 * 上传控件初始化方法事件说明:
		 * dialogOpen:() 当 选择文件 label/按钮 开始点击的时候触发此事件
		 * beforeInit:() 初始化上传控件 之前 触发此事件
		 * dndAccept:(items) 阻止此事件可以拒绝某些类型的文件拖入进来.目前只有 chrome 提供这样的 API,且只能通过 mime-type 验证
		 * beforeFileQueued:(file {File}File对象) 当文件被加入队列之前触发,此事件的handler返回值为false,则此文件不会被添加进入队列
		 * fileQueued:(file {File}File对象) 当文件被加入队列以后触发
		 * filesQueued:(files {File}数组,内容为原始File(lib/File)对象) 当一批文件添加进队列以后触发
		 * fileDequeued:(file {File}File对象) 当文件被移除队列后触发
		 * reset:() 当 uploader 被重置的时候触发
		 * startUpload:() 当开始上传流程时触发
		 * stopUpload:() 当开始上传流程暂停时触发
		 * uploadFinished:() 当所有文件上传结束时触发
		 * uploadStart:(file {File}File对象) 某个文件开始上传前触发,一个文件只会触发一次
		 * uploadBeforeSend:(object {Object},data {Object}默认的上传参数可以扩展此对象来控制上传参数,headers {Object}可以扩展此对象来控制上传头部)
		 * 当某个文件的分块在发送前触发,主要用来询问是否要添加附带参数,大文件在开起分片上传的前提下此事件可能会触发多次
		 * uploadAccept:(object {Object},
		 * ret {Object}服务端的返回数据，json格式，如果服务端不是json格式，从ret._raw中取数据，自行解析,
		 * setFailReason {String} 用来改写reject(也就是type)，只有当type符合http或abort出现错误时才会自动重传)
		 * 当某个文件上传到服务端响应后,会派送此事件来询问服务端响应是否有效,如果此事件handler返回值为false,则此文件将派送server类型的uploadError事件
		 * uploadProgress:(file {File}File对象,percentage {Number}上传进度) 上传过程中触发,携带上传进度。
		 * uploadError:(file {File}File对象,reason {String}出错的code) 当文件上传出错时触发
		 * uploadSuccess:(file {File}File对象,response {Object}服务端返回的数据) 当文件上传成功时触发。
		 * uploadComplete:(file {File} [可选]File对象) 不管成功或者失败,文件上传完成时触发
		 * error:(type {String}错误类型) 当validate不通过时，会以派送错误事件的形式通知调用者。目前有以下错误会在特定的情况下派送错来。
		 * Q_EXCEED_NUM_LIMIT 在设置了fileNumLimit且尝试给uploader添加的文件数量超出这个值时派送。
		 * Q_EXCEED_SIZE_LIMIT 在设置了Q_EXCEED_SIZE_LIMIT且尝试给uploader添加的文件总大小超出这个值时派送。
		 * Q_TYPE_DENIED 当文件类型不满足时触发。。
		 * @method uploaderInit
		 * @return {String} 返回值说明
		 */
		uploaderInit: function(options) {
			return init(options);
		},
		dialogOpen: function() {},
		beforeInit: function() {},
		dndAccept: function(items) {},
		beforeFileQueued: function(file) {},
		fileQueued: function(file) {},
		filesQueued: function(files) {},
		fileDequeued: function(file) {},
		reset: function() {},
		startUpload: function() {},
		stopUpload: function() {},
		uploadFinished: function() {},
		uploadStart: function(file) {},
		uploadBeforeSend: function(object, data, headers) {},
		uploadAccept: function() {},
		uploadProgress: function(file, percentage) {},
		uploadError: function(file, reason) {},
		uploadSuccess: function(file, response) {},
		uploadComplete: function(file) {},
		error: function(type) {}
	})

	function init(options) {
		if (!WebUploader.Uploader.support()) {
			let error = "上传控件不支持您的浏览器！请尝试升级flash版本或者使用Chrome引擎的浏览器";
			if (window.console) {
				window.console.error(error);
			}
			return;
		}
		options = options || {};
		// 创建默认值
		let defaults = {
			swf: files.swf,
			server: files.server,
			dnd: undefined,
			disableGlobalDnd: false,
			paste: document.body,
			pick: {
				id: '',
				innerHTML: '',
				multiple: false
			},
			accept: {
				title: '',
				extensions: '',
				mimeTypes: ''
			},
			thumb: {
				width: 300,
				height: 300,
				quality: 70,
				allowMagnify: false,
				crop: true,
				type: undefined
			},
			compress: {
				width: 1600,
				height: 1600,
				quality: 90,
				allowMagnify: false,
				crop: false,
				preserveHeaders: true,
				noCompressIfLarger: false,
				compressSize: 1048576
			},
			auto: false,
			runtimeOrder: 'html5,flash',
			prepareNextFile: true,
			chunked: false,
			chunkSize: 5242880,
			chunkRetry: 2,
			threads: 3,
			formData: {},
			fileVal: 'file',
			method: 'POST',
			sendAsBinary: false,
			fileNumLimit: undefined,
			fileSizeLimit: undefined,
			fileSingleSizeLimit: undefined,
			duplicate: false,
			disableWidgets: undefined,
			dialogOpen: function() {},
			beforeInit: function() {},
			dndAccept: function(items) {},
			beforeFileQueued: function(file) {},
			fileQueued: function(file) {},
			filesQueued: function(files) {},
			fileDequeued: function(file) {},
			reset: function() {},
			startUpload: function() {},
			stopUpload: function() {},
			uploadFinished: function() {},
			uploadStart: function(file) {},
			uploadBeforeSend: function(object, data, headers) {},
			uploadAccept: function() {},
			uploadProgress: function(file, percentage) {},
			uploadError: function(file, reason) {},
			uploadSuccess: function(file, response) {},
			uploadComplete: function(file) {},
			error: function(type) {}
		}

		// 合并json
		Object.assign(defaults, options);

		defaults.beforeInit();
		$.beforeInit();

		// 构建上传控件实例
		let uploader = WebUploader.create(defaults);

		uploader.on('dialogOpen', function() {
			defaults.dialogOpen();
			$.dialogOpen();
		});
		uploader.on('dndAccept', function(items) {
			defaults.dndAccept(items);
			$.dndAccept(items);
		});
		uploader.on('beforeFileQueued', function(file) {
			let beforeFileQueued = defaults.beforeFileQueued(file);
			beforeFileQueued = $.beforeFileQueued(file);
			if (beforeFileQueued === false) {
				return false;
			}
		});
		uploader.on('fileQueued', function(file) {
			defaults.fileQueued(file);
			$.fileQueued(file);
		});
		uploader.on('filesQueued', function(files) {
			defaults.filesQueued(files);
			$.filesQueued(files);
		});
		uploader.on('fileDequeued', function(file) {
			defaults.fileDequeued(file);
			$.fileDequeued(file);
		});
		uploader.on('reset', function() {
			defaults.reset();
			$.reset();
		});
		uploader.on('startUpload', function() {
			defaults.startUpload();
			$.startUpload();
		});
		uploader.on('stopUpload', function() {
			defaults.stopUpload();
			$.stopUpload();
		});
		uploader.on('uploadFinished', function() {
			defaults.uploadFinished();
			$.uploadFinished();
		});
		uploader.on('uploadStart', function(file) {
			// 上一次上传的时间节点、上一次已经上传大小(单位字节)、历史上传速率、平均速率
			files[file.id] = {
				startUploadTime: Date.now(),
				fileUploadedSize: 0
			}
			defaults.uploadStart(file);
			$.uploadStart(file);
		});
		uploader.on('uploadBeforeSend', function(object, data, headers) {
			defaults.uploadBeforeSend(object, data, headers);
			$.uploadBeforeSend(object, data, headers);
		});
		uploader.on('uploadAccept', function() {
			defaults.uploadAccept();
			$.uploadAccept();
		});

		uploader.on('uploadProgress', function(file, percentage) {
			// 计算当前上传速率，分别计算出时间差(以秒为单位)、该时间差内上传的文件大小(以字节为单位)
			let timeSpan = (Date.now() - files[file.id].startUploadTime) / 1000;
			let fileDifference = (file.size * percentage) - files[file.id].fileUploadedSize;
			// 用 文件差 / 时间差 = 单位 b/s
			let uploadRate = fileDifference / timeSpan;

			if (file.uploadRate === undefined) {
				file.uploadRate = 0;
			}

			// 每 秒 更新一次
			if (timeSpan >= 1) {
				const average = arr => arr.reduce((acc, val) => acc + val, 0) / arr.length;
				// 重置上传时间
				files[file.id].startUploadTime = new Date().getTime();
				files[file.id].fileUploadedSize = file.size * percentage;
				// 上传速率(b/s)
				file.uploadRate = Math.max(uploadRate, 0);
			}

			defaults.uploadProgress(file, percentage);
			$.uploadProgress(file, percentage);
		});
		uploader.on('uploadError', function(file, reason) {
			defaults.uploadError(file, reason);
			$.uploadError(file, reason);
		});
		uploader.on('uploadSuccess', function(file, response) {
			defaults.uploadSuccess(file, response);
			$.uploadSuccess(file, response);
		});
		uploader.on('uploadComplete', function(file) {
			defaults.uploadComplete(file);
			$.uploadComplete(file);
		});
		uploader.on('error', function(type) {
			defaults.error(type);
			$.error(type);
			if (type === "Q_EXCEED_NUM_LIMIT") {
				console.error('文件数量超出限制');
			} else if (type === "Q_TYPE_DENIED") {
				console.error('文件类型不满足条件');
			} else if (type === "Q_EXCEED_SIZE_LIMIT") {
				console.error('文件总大小超出限制');
			} else if (type === "F_DUPLICATE") {
				console.error('请勿重复添加');
			} else if (type === "F_EXCEED_SIZE") {
				console.error('文件过大');
			} else {
				console.error(type);
			}
		});
		return uploader;
	}
})(jQuery);

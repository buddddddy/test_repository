var gulp               = require('gulp');
var less               = require('gulp-less');
var concat             = require('gulp-concat');
var webpack            = require('webpack');
var minifyCSS          = require('gulp-minify-css');
var autoprefixer       = require('gulp-autoprefixer');
var cssBase64          = require('gulp-css-base64');
var gutil              = require('gutil');
var path               = require('path');
var browserSync        = require('browser-sync').create();
var commandLineArgs    = require('command-line-args');
var CleanWebpackPlugin = require('clean-webpack-plugin');
var pug                = require('gulp-pug');
var fs                 = require('fs');

// для того чтобы включить proxy надо запускать c ключом -p
var options = function () {
    if (this.opts === undefined) {
        this.opts = commandLineArgs([
            {name: 'proxy', alias: 'p', type: Boolean, defaultOption: false}
        ]);
    }
    return this.opts;
};

const src_dir = './';
//const res_dir = '../../../build/resources/main/static';
const res_dir = '../../../build/ui/static';

var localSettings = {
    server: {
        baseDir: res_dir,
        index: "./login.html"
    }
};


gulp.task('browser-sync', function () {
    if (options().proxy) {
        browserSync.init(proxySettings);
    } else {
        browserSync.init(localSettings);
    }
});

/* COMMON CSS BUILDER */
function cssBuilder (srcName, cssName) {
    return function (cb) {
        return gulp.src(path.join(__dirname, src_dir, 'style', srcName))
            .pipe(less())
            .pipe(cssBase64({maxWeightResource: 10000000})) //
            .pipe(concat(cssName))
            .pipe(autoprefixer('last 10 versions', 'ie 9'))
            .pipe(minifyCSS({keepBreaks: false}))
            .pipe(gulp.dest(path.join(__dirname, res_dir, 'css')));
    }
}

/* COMMON JS BUILDER */
function jsBuilder (srcName, jsName) {
    return function (cb) {
        webpack({
            entry: ['babel-polyfill', path.join(__dirname, src_dir, 'js', srcName)],
            output: {path: path.join(__dirname, res_dir, 'js'), filename: jsName},
            resolve: {
                alias: {
                    'jquery': path.join(__dirname, 'node_modules/jquery/dist/jquery')
                    , 'inputmask.dependencyLib': path.join(__dirname, 'node_modules/jquery.inputmask/dist/inputmask/inputmask.dependencyLib')
                    , 'inputmask' : path.join(__dirname, 'node_modules/jquery.inputmask/dist/inputmask/inputmask')
                    , 'jquery.inputmask': path.join(__dirname, 'node_modules/jquery.inputmask/dist/inputmask/jquery.inputmask')
                    , 'inputmask.numeric.extensions': path.join(__dirname, 'node_modules/jquery.inputmask/dist/inputmask/inputmask.numeric.extensions')
                    , 'inputmask.extensions': path.join(__dirname, 'node_modules/jquery.inputmask/dist/inputmask/inputmask.extensions')
                    , 'inputmask.date.extensions': path.join(__dirname, 'node_modules/jquery.inputmask/dist/inputmask/inputmask.date.extensions')
                    , 'json.viewer': path.join(__dirname, 'node_modules/jquery.json-viewer/json-viewer/jquery.json-viewer')
                },
                extensions: ['.js', '.jsx']
            },
            plugins: [
                new webpack.optimize.OccurrenceOrderPlugin(),
                //new webpack.DefinePlugin({
                //    'process.env': {
                //        NODE_ENV: JSON.stringify('production')
                //    }
                //}),
                //new webpack.optimize.UglifyJsPlugin({
                //    compress: {
                //        warnings: false,
                //        screw_ie8: true,
                //        conditionals: true,
                //        unused: true,
                //        comparisons: true,
                //        sequences: true,
                //        dead_code: true,
                //        evaluate: true,
                //        if_return: true,
                //        join_vars: true
                //    },
                //    output: {
                //        comments: false
                //    },
                //    sourceMap: true
                //})
            ],
            module: {
                loaders: [
                    { test: /\.jsx?$/, loader: ['babel-loader'], exclude: [/node_modules/, RegExp(res_dir)] }
                ]
            },
            devtool: 'source-map'
        }, function (err, stats) {
            if (err) throw new gutil.PluginError('webpack', err);
            gutil.log('[webpack]', stats.toString({/* output options */}));
            //fs.writeFile('stats.json', JSON.stringify(stats.toJson()), cb);
            cb();
        });
        return gulp;
    };
}

/* COMMON HTML BUILDER */
function htmlBuilder (srcName, htmlName) {
    return function (cb) {
        return gulp.src(path.join(__dirname, src_dir, 'pug', srcName))
            .pipe(pug({ verbose: true }))
            .pipe(concat(htmlName))
            .pipe(gulp.dest(path.join(__dirname, res_dir)));
    };
}


/* CREATE OF TASKS */
const appList = ['analytics', 'login', 'events_list', 'kiz_list', 'kiz_info'
    , 'kiz_events', 'kiz_timeline', 'lp_list', 'members_list', 'member_card'
    , 'reestr_gs1', 'reestr_esklp', 'reestr_licenses', 'reestr_pharm'];

for (var i=0; i<appList.length; i++) {
    var app = appList[i];
    gulp.task('build-' + app + '-html', htmlBuilder(app + '.pug' , app + '.html'));
    gulp.task('build-' + app + '-css' ,  cssBuilder(app + '.less', app + '.css'));
    gulp.task('build-' + app + '-js'  ,   jsBuilder(app + '.js'  , app + '.js'));
}

/* COMMON WATCHER */
gulp.task('watch', function () {
    gulp.watch([path.join(__dirname, src_dir,   'pug/**/*')], appList.map(function (app) { return 'build-' + app + '-html'; }));
    gulp.watch([path.join(__dirname, src_dir, 'style/**/*')], appList.map(function (app) { return 'build-' + app + '-css'; }));
    gulp.watch([path.join(__dirname, src_dir,    'js/**/*')], appList.map(function (app) { return 'build-' + app + '-js'; }));
	gulp.watch([path.join(__dirname, res_dir,       '**/*')], function () { browserSync.reload(); });
	return gulp;
});

/* MAIN TASKS */
gulp.task(
    'build',
    appList.map(function (app) { return 'build-' + app + '-html'; })
        .concat(appList.map(function (app) { return 'build-' + app + '-css'; }))
        .concat(appList.map(function (app) { return 'build-' + app + '-js'; }))
);
gulp.task('default', ['browser-sync', 'watch']);

var gulp = require('gulp');
var sass = require('gulp-sass');
var minifyCss = require('gulp-minify-css');
var sourceMaps = require('gulp-sourcemaps');
var rename = require('gulp-rename');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');

var distLocation = 'src/main/resources/jcr_root/etc/slick/designs/slick/dist';

gulp.task('styles', function() {
	gulp.src('./**/scss/*.scss', {base: '.'})
		.pipe(sourceMaps.init())
		.pipe(sass().on('error', sass.logError))
		.pipe(rename(function (path) {
			path.dirname += "/../css";
			return path;
		}))
		.pipe(minifyCss())
		.pipe(sourceMaps.write('.'))
		.pipe(gulp.dest('.'))
});

// gulp.task('scripts', function() {
// 	gulp.src('./**/js/publish/*.js')
// 		.pipe(concat('scripts.js'))
// 		.pipe(gulp.dest(distLocation + '/js'))
// 		.pipe(rename('scripts.min.js'))
// 		.pipe(uglify())
// 		.pipe(gulp.dest(distLocation + '/js'));
// });

//Watch task
gulp.task('default',function() {
    gulp.watch('./**/scss/*.scss',['styles']);
    // gulp.watch('./**/js/publish/*.js',['scripts']);
});
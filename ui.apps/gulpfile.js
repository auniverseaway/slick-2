var gulp = require('gulp');
var sass = require('gulp-sass');
var minifyCss = require('gulp-minify-css');
var sourceMaps = require('gulp-sourcemaps');
var rename = require("gulp-rename");

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

//Watch task
gulp.task('default',function() {
    gulp.watch('./**/scss/*.scss',['styles']);
});
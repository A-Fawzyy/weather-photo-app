package com.example.weatherpictureapp.core

/**
 * Base class for use cases.
 *
 * [T] the type of the [BaseUseCase] result
 *
 * [S] the type of the input parameters
 *
 * For example, if the useCase is to get a list of photos.
 * then [T] will be a [List<Movie>] and [S] will be the [pageNumber]
 */
interface BaseUseCase<T, S> {

	/**
	 * Executes and invokes the use case.
	 *
	 *  [params] the input parameters to run the use case with
	 *  Returns the [T] result of running the [BaseUseCase]
	 */
	operator fun invoke(params: S): T
}

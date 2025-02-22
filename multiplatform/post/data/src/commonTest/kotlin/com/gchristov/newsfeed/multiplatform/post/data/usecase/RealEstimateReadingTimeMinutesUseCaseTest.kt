package com.gchristov.newsfeed.multiplatform.post.data.usecase

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.common.test.FakeCoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class RealEstimateReadingTimeMinutesUseCaseTest {

    @Test
    fun returnsZeroMinutesWhenEmptyText() {
        val text = "";
        runTest { useCase ->
            val actualResult = useCase.invoke(EstimateReadingTimeMinutesUseCase.Dto(text))
            assertEquals(
                expected = Either.Right(0),
                actual = actualResult
            )
        }
    }

    @Test
    fun returnsZeroMinutesWhenNoWords() {
        val text = "    ";
        runTest { useCase ->
            val actualResult = useCase.invoke(EstimateReadingTimeMinutesUseCase.Dto(text))
            assertEquals(
                expected = Either.Right(0),
                actual = actualResult
            )
        }
    }

    @Test
    fun takesMinimumOneMinuteWhenLessThanTwoHundredWords() {
        val text = "one two three";
        runTest { useCase ->
            val actualResult = useCase.invoke(EstimateReadingTimeMinutesUseCase.Dto(text))
            assertEquals(
                expected = Either.Right(1),
                actual = actualResult
            )
        }
    }

    @Test
    fun takesOneMinuteForExactlyTwoHundredWords() {
        runTest { useCase ->
            val actualResult = useCase.invoke(EstimateReadingTimeMinutesUseCase.Dto(Text200))
            assertEquals(
                expected = Either.Right(1),
                actual = actualResult
            )
        }
    }

    @Test
    fun roundsUpMinutesWhenPassingThreshold() {
        runTest { useCase ->
            // 300 words is 1m30s, given 200 words is 1m. 301 words would go above the threshold
            // so we check that the minutes have rounded to 2.
            val actualResult = useCase.invoke(EstimateReadingTimeMinutesUseCase.Dto(Text301))
            assertEquals(
                expected = Either.Right(2),
                actual = actualResult
            )
        }
    }

    private fun runTest(
        testBlock: suspend CoroutineScope.(EstimateReadingTimeMinutesUseCase) -> Unit
    ) = runBlocking {
        testBlock(RealEstimateReadingTimeMinutesUseCase(dispatcher = FakeCoroutineDispatcher))
    }
}

private const val Text200 = """
    Lorem ipsum dolor sit amet consectetur adipiscing elit mi volutpat, lacus erat nisl vivamus per orci 
    parturient turpis platea nulla, pulvinar montes eros phasellus curae morbi nunc diam. Tincidunt varius 
    nullam leo fusce sagittis faucibus primis curabitur, pharetra praesent netus sociosqu blandit vehicula 
    luctus sollicitudin nibh, fringilla suspendisse vel non neque dictum duis. Dignissim ut quisque dapibus 
    auctor semper sed cum litora feugiat hendrerit, in nostra a aliquam ac porttitor nec imperdiet venenatis 
    gravida pretium, sapien ante et malesuada sem enim eu est interdum. Quis torquent fermentum commodo 
    lacinia class potenti rhoncus ridiculus consequat, himenaeos penatibus quam felis vestibulum lectus 
    sociis vitae, ornare proin inceptos viverra vulputate mollis laoreet facilisi. At fames placerat 
    magnis purus accumsan cubilia massa natoque augue, maecenas justo pellentesque bibendum habitant 
    metus donec nisi mauris iaculis, egestas ligula dui mattis etiam molestie habitasse ultricies. Magna 
    scelerisque suscipit dis velit ad odio posuere, lobortis congue porta condimentum tellus conubia 
    senectus hac, tempor urna eleifend id libero tempus. Sodales facilisis arcu aptent aliquet nam dictumst 
    tortor, cursus nascetur risus taciti convallis cras rutrum elementum, euismod ullamcorper mus integer 
    tristique ultrices. Aenean eget feugiat pretium vulputate facilisis varius, fames interdum volutpat 
    vestibulum phasellus himenaeos, quisque pulvinar mi porta ullamcorper.
"""

private const val Text301 = """
    Lorem ipsum dolor sit amet consectetur adipiscing elit curae, cursus quis congue litora curabitur 
    himenaeos convallis, mollis imperdiet eget luctus scelerisque volutpat elementum. Neque accumsan 
    condimentum egestas tempor vivamus dignissim ut fringilla commodo, conubia porttitor aliquam sapien 
    torquent erat risus mattis duis, natoque ornare nibh dapibus orci in turpis laoreet, proin tempus 
    molestie magna tortor venenatis etiam fermentum. Parturient platea placerat lectus a vitae primis 
    pharetra, taciti vel bibendum nunc phasellus senectus rutrum, integer ullamcorper semper arcu 
    rhoncus nec. Euismod vulputate augue dictumst vehicula suscipit praesent aenean cubilia nascetur, 
    feugiat dis justo ac dictum tellus enim aptent facilisis, montes tincidunt magnis ultrices habitasse 
    iaculis lobortis hendrerit. Netus sed eleifend felis nam quisque inceptos nostra porta ultricies 
    pellentesque, lacinia hac velit sem leo tristique est at habitant, fames cum pretium id aliquet 
    vestibulum mus viverra cras. Massa malesuada sagittis potenti nulla donec maecenas non ante 
    faucibus, sociosqu odio morbi eros blandit dui posuere sollicitudin metus, urna nullam facilisi 
    ridiculus lacus varius diam et. Mi nisl ligula sociis consequat gravida eu quam nisi suspendisse 
    per purus sodales pulvinar class libero, interdum penatibus auctor fusce ad mauris facilisis 
    imperdiet pharetra orci nunc cras cum. At per vestibulum himenaeos viverra habitasse parturient 
    odio ligula lectus malesuada habitant litora, montes rutrum penatibus neque potenti scelerisque 
    ut non ridiculus phasellus. Tempus augue curabitur sociosqu convallis eget lacus, morbi nisl 
    suscipit primis dignissim fringilla sem, interdum lacinia netus bibendum maecenas. Aenean vitae 
    leo vehicula congue suspendisse dictum commodo et dui hendrerit, turpis cursus taciti faucibus 
    arcu laoreet risus ad accumsan mollis, facilisi massa mus placerat luctus pellentesque torquent 
    quam inceptos. Sollicitudin auctor tincidunt sodales tristique tortor quisque nulla velit vivamus, 
    praesent ac sed nec dis aliquet donec consequat. Porta mauris gravida erat nisi fusce nibh curae, 
    feugiat elementum libero aliquam senectus in, metus dictumst enim porttitor duis class.
"""
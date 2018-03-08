package uk.ac.ebi.eva.benchmarking_suite.mongodb

import org.apache.jmeter.samplers.{AbstractSampler, Entry, SampleResult}
import org.apache.jmeter.util.JMeterUtils
import org.mongodb.scala.Document
import org.mongodb.scala.model._
import uk.ac.ebi.eva.benchmarking_suite.{DBSamplerProcessor, JMeterSamplerContextProperties}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MongoDBWriteSampler() extends AbstractSampler {

  val defaultBatchSize = 1000
  val bulkWriteOptions: BulkWriteOptions = BulkWriteOptions().ordered(false)
  type doc = WriteModel[_ <: Document]

  var mongoDBTestParams: MongoDBConnectionParams = _

  override def sample(entry: Entry): SampleResult = {
    DBSamplerProcessor.process(sampler = this,
      databaseAction = () => {
        mongoDBTestParams = JMeterUtils.getJMeterProperties.get("connectionParams").asInstanceOf[MongoDBConnectionParams]
        val numInsertsPerThread = this.getPropertyAsInt("numOpsPerThread")
        val samplerCtxProps = new JMeterSamplerContextProperties(this.getPropertyAsInt("threadChoice") ,
          this.getThreadContext.getThreadNum, this.getThreadContext.getVariables.getIteration)
        insertData(samplerCtxProps, counter = 0, numInsertsPerThread, List[doc]())
      })
  }

  @annotation.tailrec
  private def insertData(samplerCtxProps: JMeterSamplerContextProperties, counter: Int, numInsertsPerThread: Int,
                         documents: List[doc]): Unit = {
    def batchWrite = {
      Await.result(mongoDBTestParams.mongoCollection.bulkWrite(documents, bulkWriteOptions).toFuture(),
        Duration.Inf)
    }

    if (counter < numInsertsPerThread) {
      //Accession and Entity IDs suffix format
      //Total number of threads for the current sampler
      //Thread number of the current sampler (for ex: has a value 6 for the 6th thread of a sampler running with 8 threads)
      //Iteration number of the current sampler (how many times has the current sampler already run thus far + 1)
      //Index of the record currently being inserted
      val accessionId = "acc_%d_%d_%d_%d".format(samplerCtxProps.threadChoice, samplerCtxProps.threadNum,
        samplerCtxProps.loopIndex, counter)
      val entityId = "ent_%d_%d_%d_%d".format(samplerCtxProps.threadChoice, samplerCtxProps.threadNum,
        samplerCtxProps.loopIndex, counter)
      val documentToInsert: doc = InsertOneModel(Document(
        "_id" -> accessionId, "species" -> "eva_hsapiens_grch37", "chromosome" -> samplerCtxProps.threadNum.toString,
        "start_pos" -> (counter + 100), "entity_id" -> entityId,
        "accession_id" -> accessionId, "raw_numeric_id" -> counter)
      )
      val timeForBatchWrite = counter % defaultBatchSize == 0 && counter > 0
      if (timeForBatchWrite) {
        batchWrite
      }
      insertData(samplerCtxProps, counter + 1, numInsertsPerThread,
        documentToInsert :: (if (timeForBatchWrite) List[doc]() else documents))
    }
    else {
      batchWrite
    }
  }
}
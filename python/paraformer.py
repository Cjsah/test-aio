from http import HTTPStatus
import dashscope
import json

dashscope.api_key='your-dashscope-api-key'

task_response=dashscope.audio.asr.Transcription.async_call(
    model='paraformer-v2',
    file_urls=['https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_female2.wav',
               'https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_male2.wav'],
    language_hints=['zh', 'en']
)

transcribe_response=dashscope.audio.asr.Transcription.wait(task=task_response.output.task_id)
if transcribe_response.status_code == HTTPStatus.OK:
    print(json.dumps(transcribe_response.output, indent=4, ensure_ascii=False))
    print('transcription done!')
import librosa, os
from scipy.spatial.distance import euclidean
from scipy.spatial.distance import cosine
from fastdtw import fastdtw
import numpy as np


def load_files(path):
    return [f for f in os.scandir(path) if f.is_file() and f.name.endswith('.wav')]


# 加载音频文件
def load_audio(filename):
    y, sr = librosa.load(filename)
    y = librosa.util.normalize(y)
    y = y / np.max(np.abs(y))
    mfcc = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=13)
    chroma = librosa.feature.chroma_stft(y=y, sr=sr)
    spectral_contrast = librosa.feature.spectral_contrast(y=y, sr=sr)
    # tonnetz = librosa.feature.tonnetz(y=y, sr=sr)
    features = np.concatenate((mfcc, chroma, spectral_contrast), axis=0)
    features = (features - np.mean(features, axis=1, keepdims=True)) / np.std(features, axis=1, keepdims=True)
    return features.T


# 比较两个MFCC序列的相似度
def compare_mfcc(mfcc1, mfcc2):
    # 使用DTW来对比两个MFCC特征
    distance, path = fastdtw(mfcc1, mfcc2, dist=cosine)
    return distance


# 主程序
def main():
    ref_mfcc = load_audio('run/template/cat.wav')
    print("===")
    user_mfcc = load_audio('run/table/张.wav')
    distance = compare_mfcc(ref_mfcc, user_mfcc)
    print(distance)
    print("=====")
    return

    files = load_files('run/template')

    for file in files:
        name = file.name[:-4]
        audios = load_files(f'run/{name}')
        if len(audios) == 0:
            continue
        print(f'正在匹配: {name}')
        ref_mfcc = load_audio(file.path)
        for audio in audios:
            user_mfcc = load_audio(audio.path)
            distance = compare_mfcc(ref_mfcc, user_mfcc)
            print(f'{audio.name[:-4]}的匹配度: {distance}')


    # ref_mfcc = load_audio('run/清晰.wav')
    # user_mfcc = load_audio('run/不清晰.wav')
    #
    # # 比较MFCC特征
    # distance = compare_mfcc(ref_mfcc, user_mfcc)
    # print(f"MFCC-based Distance: {distance}")


if __name__ == "__main__":
    main()

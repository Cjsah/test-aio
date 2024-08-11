import librosa
from scipy.spatial.distance import euclidean
from fastdtw import fastdtw


# 加载音频文件
def load_audio(filename):
    y, sr = librosa.load(filename)
    mfcc = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=13)
    return mfcc.T


# 比较两个MFCC序列的相似度
def compare_mfcc(mfcc1, mfcc2):
    # 使用DTW来对比两个MFCC特征
    distance, path = fastdtw(mfcc1, mfcc2, dist=euclidean)
    return distance

# 主程序
def main():
    ref_mfcc = load_audio('run/清晰.wav')
    user_mfcc = load_audio('run/不清晰.wav')

    # 比较MFCC特征
    distance = compare_mfcc(ref_mfcc, user_mfcc)
    print(f"MFCC-based Distance: {distance}")

if __name__ == "__main__":
    main()

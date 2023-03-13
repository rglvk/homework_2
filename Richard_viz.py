import argparse
import re
import matplotlib.pyplot as plt

def run_main(args):

    plot_points(args.in_file, args.out_file)

def plot_points(in_file, out_file):

    file = open(in_file, 'r')

    x = []
    y = []

    for line in file:
        values = re.split('\s+', line.strip())
        if len(values) == 2:
            x.append(float(values[0]))
            y.append(float(values[1]))

    file.close()

    plt.figure()

    figure, axes = plt.subplots(figsize = (6.4, 6.4))

    axes.set_xlim([-1, 1])
    axes.set_ylim([-1, 1])

    circle = plt.Circle((0, 0), 1, fill = False)
    axes.add_patch(circle)

    plt.scatter(x, y, facecolors = 'none', edgecolors = 'red')

    plt.savefig(out_file)
    plt.close()

# read command line arguments and call main
if __name__ == '__main__':

    parser = argparse.ArgumentParser('Plot points')

    parser.add_argument('in_file',
                        type = str,
                        default = '',
                        help = 'Specify input file')

    parser.add_argument('out_file',
                        type = str,
                        default = '',
                        help = 'Specify output file')

    args = None
    args, unparsed = parser.parse_known_args()

    run_main('onemax_summary_copy.txt', 'output.txt')